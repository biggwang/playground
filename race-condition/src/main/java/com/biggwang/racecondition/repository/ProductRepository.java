package com.biggwang.racecondition.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Product s where s.id = :id")
    Optional<Product> findByWithPessimisticLock(@Param("id") Integer id);
}