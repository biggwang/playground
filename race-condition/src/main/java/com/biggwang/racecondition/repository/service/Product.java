package com.biggwang.racecondition.repository.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Slf4j
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    private Integer maxQuantity;

    private Integer currentQuantity;

    public Product(String name, Integer maxQuantity) {
        this.name = name;
        this.maxQuantity = maxQuantity;
        this.currentQuantity = 0;
    }

    public void order() {
        if (currentQuantity < maxQuantity) {
            ++currentQuantity;
            log.info("구매 성공, 현재 갯수: {}, 남은 갯수: {}", currentQuantity, maxQuantity - currentQuantity);
            return;
        }
        log.error("품절 ㅠ.ㅠ");
    }

    public Product ordered(int ordered) {
        this.currentQuantity = ordered;
        return this;
    }
}