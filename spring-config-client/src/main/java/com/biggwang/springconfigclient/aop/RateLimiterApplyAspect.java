package com.biggwang.springconfigclient.aop;

import com.biggwang.springconfigclient.configswitch.RateLimiterConfigProperty;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimiterApplyAspect {

    private final RateLimiterRegistry rateLimiterRegistry;
    private final RateLimiterEnableChecker rateLimiterEnableChecker;

    @Around("@annotation(com.biggwang.springconfigclient.aop.RateLimiterApply)")
    public Object handler(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        RateLimiterApply myAnnotation = method.getAnnotation(RateLimiterApply.class);
        return applyLateLimiterIfEnable(pjp, myAnnotation);
    }

    private Object applyLateLimiterIfEnable(ProceedingJoinPoint pjp, RateLimiterApply myAnnotation) throws Throwable {
        if (rateLimiterEnableChecker.isTrigger(myAnnotation.rateLimiterName())) {
            return rateLimiterRegistry.rateLimiter(myAnnotation.rateLimiterName()).executeCallable(
                    () -> {
                        try {
                            return pjp.proceed();
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        return pjp.proceed();
    }
}
