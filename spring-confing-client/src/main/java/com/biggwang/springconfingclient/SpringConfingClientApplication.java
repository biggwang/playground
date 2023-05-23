package com.biggwang.springconfingclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(MyConfig.class)
@SpringBootApplication
public class SpringConfingClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfingClientApplication.class, args);
    }

}
