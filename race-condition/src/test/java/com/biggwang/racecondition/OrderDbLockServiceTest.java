package com.biggwang.racecondition;

import com.biggwang.racecondition.helper.RaceConditionAssertHelper;
import com.biggwang.racecondition.repository.Product;
import com.biggwang.racecondition.repository.ProductRepository;
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
class OrderDbLockServiceTest {

    @Autowired
    private OrderDbLockService orderDbLockService;
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
    public void 디비락으로_레이스컨디션_테스트() throws Exception {
        helper.request(thread, es, barrier, productId, (p) -> orderDbLockService.order(productId));
    }
}