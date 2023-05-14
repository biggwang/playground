package com.biggwang.racecondition.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAop {
    private final RedissonClient redissonClient;
    private final RedissonCallTransaction redissonCallTransaction;

    @Around("@annotation(com.biggwang.racecondition.redis.RedissonLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RedissonLock redissonLock = method.getAnnotation(RedissonLock.class);

        String key = joinPoint.getArgs()[0].toString();
        RLock rock = redissonClient.getLock(key);

        try {
            boolean isPossible = rock.tryLock(redissonLock.waitTime(), redissonLock.leaseTime(), redissonLock.timeUnit());
            if (!isPossible) {
                log.warn("락 획득 실패");
                return false;
            }
            return redissonCallTransaction.proceed(joinPoint);
        } finally {
            log.info("락 해제");
            rock.unlock(); // 중요!! line 46 보다 나중에 락을 해제해야 더티 리드가 생기지 않는다.
        }
    }

    /**
     * Redisson Key Create
     * @param parameterNames
     * @param args
     * @param key
     * @return
     */
    private String createKey(String[] parameterNames, Object[] args, String key) {
        String resultKey = key;

        /* key = parameterName */
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(key)) {
                resultKey += args[i];
                break;
            }
        }

        return resultKey;
    }
}