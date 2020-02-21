package cn.weijia.tradingmonitor.platform;


import cn.weijia.tradingmonitor.dto.BitmexMsgDto;
import cn.weijia.tradingmonitor.repository.BitmexTradeDataRepository;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.*;

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
                    BitmexMsgDto bitmexMsgDto = JSON.parseObject(msg, BitmexMsgDto.class);
                    if ("insert".equals(bitmexMsgDto.getAction())) {
                        dataRepo.saveBatch(bitmexMsgDto.getData());
                        //TODO: 讲大额订单存放至另外的表. 然后积累到一定程度进行微信/电话告警
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
                }
            };
            if (proxyEnabled) {
                socketClient.setProxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(proxyAddr, proxyPort)));
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
}
