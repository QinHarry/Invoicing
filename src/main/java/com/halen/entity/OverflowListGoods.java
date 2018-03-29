package com.halen.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_overflowListGoods")
public class OverflowListGoods {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "overflowListId")
    private OverflowList overflowList;

    @Column(length = 50)
    private String code;

    @Column(length = 50)
    private String name;

    @Column(length = 50)
    private String model;

    @ManyToOne
    @JoinColumn(name = "typeId")
    private GoodsType type;

    @Transient
    private Integer typeId;

    private Integer goodsId;

    @Column(length = 10)
    private String unit;

    private Float price;

    private Integer num;

    private Float total;
}
