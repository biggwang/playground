package com.biggwang.springconfigclient;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Setter
@Getter
@ConfigurationProperties("transfer.friend")
@RefreshScope
@ToString
public class FriendTransferSwitch {

    private String enable;

}