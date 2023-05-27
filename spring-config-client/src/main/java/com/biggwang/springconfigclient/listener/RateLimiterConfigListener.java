package com.biggwang.springconfigclient.listener;

import com.biggwang.springconfigclient.configswitch.RateLimiterConfigProperty;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateLimiterConfigListener {

    private final RateLimiterRegistry registry;
    private final RateLimiterConfigProperty ratelimiterConfigProperty;

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh(RefreshScopeRefreshedEvent event) {
        log.warn("##### onRefresh {} {}", event.getName(), event.getSource());
        ratelimiterConfigProperty.targets.forEach(config -> updateRateLimits(config.getName(), config.getLimitForPeriod(), config.getTimeoutDuration()));
    }

    private void updateRateLimits(String rateLimiterName, int newLimitForPeriod, Duration newTimeoutDuration) {
        RateLimiter limiter = registry.rateLimiter(rateLimiterName);
        limiter.changeLimitForPeriod(newLimitForPeriod);
        limiter.changeTimeoutDuration(newTimeoutDuration);
    }
}