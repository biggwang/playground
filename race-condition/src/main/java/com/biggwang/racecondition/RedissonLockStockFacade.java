package com.biggwang.racecondition;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedissonLockStockFacade {

    private final RedissonClient redissonClient;
    private final OrderRedisLockService redisLockService;

    public void orderAfterLock(int productId) {
        //key 로 Lock 객체 가져옴
        RLock lock = redissonClient.getLock(String.valueOf(productId));

        try {
            //획득시도 시간, 락 점유 시간
            boolean available = lock.tryLock(60, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
                return;
            }

            // TODO 왜 이렇게 서비스 호출 위임해야 동작 할까?
            redisLockService.order(productId);
            
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }
}