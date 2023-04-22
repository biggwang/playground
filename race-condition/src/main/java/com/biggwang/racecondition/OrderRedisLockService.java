package com.biggwang.racecondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRedisLockService {

    private final ProductRepository productRepository;

    @Transactional
    public void order(int productId) {
        Product product = productRepository.findById(productId).orElseGet(() -> {
            log.error("상품 등록 필요: {}", productId);
            throw new RuntimeException("상품부터 등록해!!");
        });
        product.order();
    }
}
