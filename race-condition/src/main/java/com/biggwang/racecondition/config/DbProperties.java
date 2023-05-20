package com.biggwang.racecondition.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;


@Getter
@ConstructorBinding
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "spring.datasource")
public class DbProperties {

    private final String driverClassName;

    private final String url;

    private final String username;

    private final String password;
}
