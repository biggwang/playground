package com.biggwang.racecondition;

import com.biggwang.racecondition.redisson.RedissonLock;
import com.biggwang.racecondition.repository.Product;
import com.biggwang.racecondition.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRedissonLockService {

    private final ProductRepository productRepository;

    @Transactional
    public void order(int productId) {
        doOrder(productId);
    }

    @RedissonLock(key = "productId", waitTime = 60)
    @Transactional // Redisson 이랑 같이 사용하면 lock 이 제대로 동작 되지 않음;; 먼저 AOP 거는 락을 걸어주면 됨
    public void orderOnAnnotation(int productId) {
        doOrder(productId);
    }

    private void doOrder(int productId) {
        // TODO 캐쉬로 대체 하자 없으면 DB 조회
        Product product = productRepository.findById(productId).orElseGet(() -> {
            log.error("상품 등록 필요: {}", productId);
            throw new RuntimeException("상품부터 등록해!!");
        });
        product.order();
    }
}
