package com.biggwang.racecondition;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue
    private int id;

    private int productId;

    private boolean soldOut = false;

    public Inventory(int productId) {
        this.productId = productId;
    }

    public void doSoldOut(int productId) {
        this.soldOut = true;
    }
}
