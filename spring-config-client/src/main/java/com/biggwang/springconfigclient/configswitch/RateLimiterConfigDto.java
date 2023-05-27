package com.biggwang.springconfigclient.configswitch;

import lombok.*;

import java.time.Duration;

@Getter
@Setter // 없으면 config-server에서 바인딩이 안 되어 부트 뜰 때 오류남
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RateLimiterConfigDto {

    private String name = "default";

    private boolean enable = false;

    private int limitForPeriod = 10;

    private String limitRefreshPeriod = "10s";

    private Duration timeoutDuration = Duration.ofSeconds(1);
}