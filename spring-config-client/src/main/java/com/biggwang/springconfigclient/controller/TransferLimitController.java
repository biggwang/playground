package com.biggwang.springconfigclient.controller;

import com.biggwang.springconfigclient.aop.RateLimiterApply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RefreshScope
@RestController
public class TransferLimitController {

    private AtomicInteger atomicInteger = new AtomicInteger();

    @GetMapping("/transfer-limit")
    @RateLimiterApply(rateLimiterName = "transfer-limit")
    public String getTransfer() {
        return  String.format("%s 요청!!", atomicInteger.incrementAndGet());
    }
}
