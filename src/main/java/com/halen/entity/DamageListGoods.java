package com.halen.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_damageListGoods")
public class DamageListGoods {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "damageListId")
    private DamageList damageList;

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
