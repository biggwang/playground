package com.biggwang.springconfigclient.configswitch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

@Setter
@Getter
@ConfigurationProperties("rate-limiter")
@RefreshScope
@ToString
public class RateLimiterConfigProperty {

    private boolean enable = false;

    public List<RateLimiterConfigDto> targets;

}