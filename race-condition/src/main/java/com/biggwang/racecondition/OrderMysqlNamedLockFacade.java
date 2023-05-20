package com.biggwang.racecondition;

import com.biggwang.racecondition.repository.lock.LockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderMysqlNamedLockFacade {

    private final LockRepository lockRepository;
    private final OrderService orderService;

    // lock을 거는 커넥션과 분리: 비지니스 로직을 처리하는 커넥션과 격리하여 락으로 인한 사이으 이펙을 막는다
    // TODO 근데 왜 order 메서드랑 같은 트랜잭션이면 락이 동작 안할까?
    @Transactional("lockTransactionManager")
    public void order(Integer productId) {
        try {
            lockRepository.getLock(productId.toString());
            orderService.order(productId);
        } catch (Exception ex) {
            log.error("", ex);
        } finally {
            lockRepository.releaseLock(productId.toString());
        }
    }
}