package com.biggwang.racecondition;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Rollback(value = false)
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductRepository productRepository;

    private int thread = 5;
    private int productId = 0;

    private ExecutorService es = Executors.newFixedThreadPool(thread);
    private CyclicBarrier barrier = new CyclicBarrier(thread);

    @BeforeEach
    public void load() {
        productRepository.deleteAll();
        productId = productRepository.save(new Product("나이키신발", thread)).getId();
    }

    @Test
    public void 동시에_상품을_주문하면_최대수량만큼만_주문되는지_테스트() throws Exception {
        IntStream.rangeClosed(1, thread).forEach(i -> {
            es.submit(() -> {
                barrier.await();
                orderService.order(productId);
                return null;
            });
        });
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        Product product = productRepository.findById(productId).orElseThrow();
        log.info("현재 수량: {}, 최대 수량: {}", product.getCurrentQuantity(), product.getMaxQuantity());
        assertEquals(product.getCurrentQuantity(), product.getMaxQuantity());
    }
}