package com.biggwang.racecondition.helper;

import com.biggwang.racecondition.repository.service.Product;
import com.biggwang.racecondition.repository.service.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@Component
@RequiredArgsConstructor
public class RaceConditionAssertHelper {

    @Autowired
    private ProductRepository productRepository;

    public void request(
            Integer thread,
            ExecutorService es,
            CyclicBarrier barrier,
            Integer productId,
            Consumer<Integer> consumer
    ) throws Exception {
        IntStream.rangeClosed(1, thread)
                .forEach(i ->
                        es.submit(() -> {
                            barrier.await();
                            consumer.accept(productId);
                            return null;
                        })
                );
        es.shutdown();
        es.awaitTermination(100, TimeUnit.SECONDS);

        Product product = productRepository.findById(productId).orElseThrow();
        log.info("현재 수량: {}, 최대 수량: {}", product.getCurrentQuantity(), product.getMaxQuantity());
        assertEquals(product.getCurrentQuantity(), product.getMaxQuantity());
    }
}
