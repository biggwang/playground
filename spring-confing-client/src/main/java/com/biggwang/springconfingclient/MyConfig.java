package com.biggwang.springconfingclient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Setter
@Getter
@ConfigurationProperties("transfer.asset")
@RefreshScope
@ToString
public class MyConfig {

    private String enable;

}