package com.biggwang.racecondition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderSyncronizedService {

    private final ProductRepository productRepository;

//    @Transactional // TODO 왜 주석 해제해야 잘동작하는지 확인 필요
    public synchronized void order(int productId) {
        Product product = productRepository.findById(productId).orElseGet(() -> {
            log.error("상품 등록 필요: {}", productId);
            throw new RuntimeException("상품부터 등록해!!");
        });
        product.order();
        productRepository.saveAndFlush(product);
    }
}
