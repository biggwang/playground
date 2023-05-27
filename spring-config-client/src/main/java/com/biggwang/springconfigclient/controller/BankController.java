package com.biggwang.springconfigclient.controller;

import com.biggwang.springconfigclient.aop.RateLimiterApply;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RefreshScope
@RestController
public class BankController {

    private AtomicInteger atomicInteger = new AtomicInteger();

    @GetMapping("/banks")
    @RateLimiterApply(rateLimiterName = "banks")
    public String getTransfer() {
        return  String.format("%s 요청!!", atomicInteger.incrementAndGet());
    }
}
