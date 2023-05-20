package com.biggwang.racecondition;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OrderRedissonFacade {

    private final RedissonClient redissonClient;
    private final OrderRedissonLockService redisLockService;

    //@Transactional
    public void doOrder(int productId) {
        RLock lock = redissonClient.getLock(String.valueOf(productId));
        try {
            boolean available = lock.tryLock(60, 1, TimeUnit.SECONDS);

            if (!available) {
                System.out.println("lock 획득 실패");
                return;
            }

            /**
             * 락을 여기서 잡을거면 주문 메서드를 따로 위임해야 한다.
             * 여기서 같이 쓰려면 @Transactional을 선언해야 하는데 그러면 lock을 잡기도 전에
             * DB 커넥션을 맺고 쿼리를 조회하기 때문에 락이 제대로 동작하지 않는다.
             * 중요한 것은, 커넥션 -> 락 잡는게 아니라 락 -> 커넥션을 맺는게 핵심이다
             */
            redisLockService.order(productId);
            //doOrder(productId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }

    /*private void doOrder(int productId) {
        Product product = productRepository.findById(productId).orElseGet(() -> {
            log.error("상품 등록 필요: {}", productId);
            throw new RuntimeException("상품부터 등록해!!");
        });
        product.order();
    }*/
}