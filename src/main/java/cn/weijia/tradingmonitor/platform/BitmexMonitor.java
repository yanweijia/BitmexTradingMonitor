package cn.weijia.tradingmonitor.platform;


import cn.weijia.tradingmonitor.dto.BitmexMsgDto;
import cn.weijia.tradingmonitor.entity.BitmexTradeDataEntity;
import cn.weijia.tradingmonitor.repository.BitmexTradeDataRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BitmexMonitor implements ApplicationListener {

    @Value("${trade.platform.bitmex.trade.btcusd.socket}")
    private String xbtusdTradeSocket;

    @Resource
    private BitmexTradeDataRepository dataRepo;

    @Value("${proxy.enable}")
    private Boolean proxyEnabled;
    @Value("${proxy.address}")
    private String proxyAddr;
    @Value("${proxy.port}")
    private Integer proxyPort;

    private boolean isRunning = false;

    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine script = manager.getEngineByName("javascript");
    Invocable averageScript;
    String codeStr = "//计算成交量代码, from: https://beastlybeast.github.io/SignificantTrades/index.html\n" +
            "function average(arr, priceFilter) {\n" +
            " arr = JSON.parse(arr);" +
            "    var sumSizeArr = {}, sumPriceArr = {}, results = [], timestamp, side;\n" +
            "    for (var i = 0; i < arr.length; i++) {\n" +
            "        var item = arr[i];\n" +
            "        var key = item.timestamp + item.side;\n" +
            "        if (!(key in sumSizeArr)) {\n" +
            "            sumSizeArr[key] = {};\n" +
            "            sumSizeArr[key].sizeSum = item.size;\n" +
            "        } else {\n" +
            "            sumSizeArr[key].sizeSum += arr[i].size;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    for (var i = 0; i < arr.length; i++) {\n" +
            "        var item = arr[i];\n" +
            "        var key = item.timestamp + item.side;\n" +
            "        var sizeItem = sumSizeArr[key];\n" +
            "        if (!(key in sumPriceArr)) {\n" +
            "            sumPriceArr[key] = {};\n" +
            "            sumPriceArr[key].symbol = item.symbol;\n" +
            "            sumPriceArr[key].priceSum = item.price * item.size / sizeItem.sizeSum;\n" +
            "            sumPriceArr[key].sizeSum = item.size;\n" +
            "            sumPriceArr[key].timestamp = item.timestamp;\n" +
            "            sumPriceArr[key].side = item.side;\n" +
            "        } else {\n" +
            "            sumPriceArr[key].priceSum += item.price * item.size / sizeItem.sizeSum;\n" +
            "            sumPriceArr[key].sizeSum += item.size;\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    for (key in sumPriceArr) {\n" +
            "        var item = sumPriceArr[key];\n" +
            "        if (item.sizeSum >= priceFilter) {\n" +
            "            results.push({\n" +
            "                symbol: item.symbol,\n" +
            "                price: item.priceSum.toFixed(2),\n" +
            "                size: item.sizeSum,\n" +
            "                timestamp: item.timestamp,\n" +
            "                side: item.side\n" +
            "            });\n" +
            "        }\n" +
            "    }\n" +
            "    return results;\n" +
            "}" +
            "function formatAMPM(date) {\n" +
            "        var hours = date.getHours();\n" +
            "        var minutes = date.getMinutes();\n" +
            "        var seconds = date.getSeconds();\n" +
            "        var ampm = hours >= 12 ? 'pm' : 'am';\n" +
            "        hours = hours % 12;\n" +
            "        hours = hours ? hours : 12; // the hour '0' should be '12'\n" +
            "        minutes = minutes < 10 ? '0'+minutes : minutes;\n" +
            "        seconds = seconds < 10 ? '0'+seconds : seconds;\n" +
            "        var strTime = hours + ':' + minutes + ':' + seconds + ' ' + ampm;\n" +
            "        return strTime;\n" +
            "      }";

    {
        try {
            script.eval(codeStr);
            averageScript = (Invocable) script;
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }


    public synchronized void createConnect() {
        if (isRunning) {
            log.info("Bitmex socket is under listening . . .");
            return;
        }
        try {
            WebSocketClient socketClient = new WebSocketClient(new URI(xbtusdTradeSocket)) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    isRunning = true;
                    log.info("Bitmex socket Connected");
                }

                @Override
                public void onMessage(String msg) {
//                    log.info("onMessage:{}", msg);
                    if ("insert".equals(JSON.parseObject(msg).getString("action"))) {
                        BitmexTradeDataEntity dataEntity = average(JSON.parseObject(msg).getString("data"), 0L);
                        dataRepo.save(dataEntity);
                    } else {
                        log.info("{}", msg);
                    }
                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    isRunning = false;
                    log.warn("Bitmex socket closed, i:{},s:{},b:{}", i, s, b);
                }

                @Override
                public void onError(Exception e) {
                    isRunning = false;
                    log.error("Bitmex socket ERR!. msg:{}", e.getMessage(), e);
                    close();
                }
            };
            if (proxyEnabled) {
                socketClient.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddr, proxyPort)));
            }
            socketClient.connect();
        } catch (Exception e) {
            log.error("socket 连接失败, errMsg:{}", e.getMessage(), e);
        }
    }


    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationReadyEvent) {
            createConnect();
        }
    }

    public Boolean isRunning() {
        return isRunning;
    }


    public BitmexTradeDataEntity average(String arr, Long priceFilter) {
        try {
            Object average = averageScript.invokeFunction("average", arr, priceFilter);
            BitmexTradeDataEntity dataEntity = JSON.parseObject(JSON.parseObject(JSON.toJSONString(average)).getString("0"), BitmexTradeDataEntity.class);
//            log.info("result:{}", dataEntity);
            return dataEntity;
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
