package com.halen.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_goodsunit")
public class GoodsUnit {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 10)
    private String name;
}
