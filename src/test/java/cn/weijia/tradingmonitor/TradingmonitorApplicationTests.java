package cn.weijia.tradingmonitor;

import cn.weijia.tradingmonitor.platform.BitmexMonitor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class TradingmonitorApplicationTests {

    @Autowired
    private BitmexMonitor bitmexMonitor;

//    @Test
    void contextLoads() throws Exception {
        bitmexMonitor.createConnect();
    }

}
