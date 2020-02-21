package cn.weijia.tradingmonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TradingmonitorApplication {


    public static void main(String[] args) {
        SpringApplication.run(TradingmonitorApplication.class, args);
    }

}
