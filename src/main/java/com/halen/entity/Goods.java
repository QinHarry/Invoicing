package com.halen.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_goods")
public class Goods {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 50)
    private String code;

    private Integer inventoryQuantity;

    private Float lastPurchasingPrice;

    private Integer minNum;

    @Column(length = 50)
    private String model;

    @Column(length = 50)
    private String name;

    @Column(length = 200)
    private String producer;

    private Float purchasingPrice;

    @Column(length = 1000)
    private String remarks;

    private Float sellingPrice;

    private Integer state;  //0 initial state, 1 in, 2 tickets

    @Column(length = 10)
    private String unit;

    @ManyToOne
    @JoinColumn(name = "typeId")
    private GoodsType type;

    @Transient
    private String codeOrName;

    @Transient
    private Integer saleTotal;
}
