package com.biggwang.racecondition.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Order를 꼭 해줘야 함
 * @Transactional 과 우선순위가 섞여서 lock이 제대로 동작하지 않게 됨
 * 이게 싫으면 RedissonCallTransaction 처럼 별도로 위임해야 동작함
 * 아마 명시적으로 @Transactional과 LockAop를 분리하기 위함인거 같다
 */
@Order(1)
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RedissonLockAop {
    private final RedissonClient redissonClient;
    private final RedissonCallTransaction redissonCallTransaction;

    @Around("@annotation(com.biggwang.racecondition.redisson.RedissonLock)")
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
//            return redissonCallTransaction.proceed(joinPoint);
            return joinPoint.proceed();
        } finally {
            rock.unlock(); // 중요!! line 46 보다 나중에 락을 해제해야 더티 리드가 생기지 않는다.
        }
    }
}