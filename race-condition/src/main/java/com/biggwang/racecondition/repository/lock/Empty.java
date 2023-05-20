package com.biggwang.racecondition.repository.lock;

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
public class Empty {

    @Id
    @GeneratedValue
    private int id;
}
