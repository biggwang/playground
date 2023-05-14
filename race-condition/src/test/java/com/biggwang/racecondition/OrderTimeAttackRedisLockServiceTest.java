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
class OrderTimeAttackRedisLockServiceTest {

    @Autowired
    private OrderTimeAttackRedisLockService orderTimeAttackRedisLockService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private RaceConditionAssertHelper helper;

    private int thread = 10;
    private int productId = 0;

    private ExecutorService es = Executors.newFixedThreadPool(thread);
    private CyclicBarrier barrier = new CyclicBarrier(thread);

    @BeforeEach
    public void load() {
        productRepository.deleteAll();
        inventoryRepository.deleteAll();
        productId = productRepository.save(new Product("나이키신발", 1)).getId(); // 상품이
        inventoryRepository.save(new Inventory(productId));
    }

    @Test
    public void 동시에_상품을_주문하면_딱1개만_주문되는지_테스트() throws Exception {
        helper.request(thread, es, barrier, productId, (p) -> orderTimeAttackRedisLockService.order(productId));
    }
}