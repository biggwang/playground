package com.biggwang.springconfigclient.aop;

import com.biggwang.springconfigclient.configswitch.RateLimiterConfigProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimiterEnableChecker {

    private final RateLimiterConfigProperty rateLimiterConfigProperty;

        // lateLimiter 기능 적용 여부 판단
    public boolean isTrigger(String name) {
        log.info("####### rate limiter config: {}", rateLimiterConfigProperty);
        if (!rateLimiterConfigProperty.isEnable()) {
            return false;
        }
        return isEnableTarget(name);
    }

    // endpoint 별 lateLimiter 적용 여부 판단
    private boolean isEnableTarget(String name) {
        return rateLimiterConfigProperty.targets.stream()
                .anyMatch(config -> config.isEnable() && name.equals(config.getName()));
    }

}
