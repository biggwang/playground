package com.biggwang.racecondition;

import com.biggwang.racecondition.redis.RedissonLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRedisLockService {

    private final ProductRepository productRepository;

    @Transactional
    public void order(int productId) {
        doOrder(productId);
    }

    @RedissonLock(key = "productId", waitTime = 60)
    @Transactional
    public void orderOnAnnotation(int productId) {
        doOrder(productId);
    }

    private void doOrder(int productId) {
        Product product = productRepository.findById(productId).orElseGet(() -> {
            log.error("상품 등록 필요: {}", productId);
            throw new RuntimeException("상품부터 등록해!!");
        });
        product.order();
    }
}
