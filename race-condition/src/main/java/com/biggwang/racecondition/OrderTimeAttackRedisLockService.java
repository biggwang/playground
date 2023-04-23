package com.biggwang.racecondition;

import com.biggwang.racecondition.redis.RedissonLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderTimeAttackRedisLockService {

    private final InventoryRepository inventoryRepository;
    private final OrderRedisLockService orderRedisLockService;
    private final RedissonLockStockFacade redissonLockStockFacade;

//    @Transactional
    @RedissonLock(key = "productId", waitTime = 60) // 여기서 락을 걸어줘야 완벽히 베타제어가 된다.
    public void order(int productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId).orElseThrow(() -> {
            log.error("창고에 상품 없음..");
            throw new RuntimeException("창고에 상품 없음..");
        });

        if (!inventory.isSoldOut()) {
            /**
             * 락을 여기에만 잡으면 정합성 깨짐... 위 재고 테이블에서 품절인지 리드하부분 까지 잠금을 걸어줘야 완벽히 베타제어가 되는것
             * 왜냐하면 주문만 잠금을 걸으면 읽는쪽에서는 여러스레드가 결국 재고가 있다고 판단하게 되기 때문이다
             */
            orderRedisLockService.order(productId);
//            redissonLockStockFacade.orderAfterLock(productId);
            inventory.doSoldOut(productId);
        }
    }
}
