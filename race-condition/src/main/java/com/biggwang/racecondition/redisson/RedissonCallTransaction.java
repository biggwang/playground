package com.biggwang.racecondition.redisson;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

@Component
public class RedissonCallTransaction {
    /**
     * Redisson 이랑 @Transactional을 같이 사용하면 lock이 제대로 동작하지 않아서
     * 이렇게 별도 클래스에 위임하여 처리
     * 정확히 왜 그런지는 알아보자...
     */
//    @Transactional
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}