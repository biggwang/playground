package com.biggwang.racecondition;

import com.biggwang.racecondition.config.DbProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(DbProperties.class)
@SpringBootApplication
public class RaceConditionApplication {

    public static void main(String[] args) {
        SpringApplication.run(RaceConditionApplication.class, args);
    }

}
