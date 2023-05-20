package com.biggwang.racecondition;

import com.biggwang.racecondition.repository.service.Product;
import com.biggwang.racecondition.repository.service.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;

    @Transactional("serviceTransactionManager")
    public void order(int productId) {
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
