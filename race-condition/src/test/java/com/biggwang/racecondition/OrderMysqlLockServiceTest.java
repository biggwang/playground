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

@Slf4j
@Rollback(value = false)
@SpringBootTest
class OrderMysqlLockServiceTest {

    @Autowired
    private OrderMysqlLockService orderMysqlLockService;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private RaceConditionAssertHelper helper;

    private int thread = 10;
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
        helper.request(thread, es, barrier, productId, (p) -> orderMysqlLockService.order(productId));
    }
}