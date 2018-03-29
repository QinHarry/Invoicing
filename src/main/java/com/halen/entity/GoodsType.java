package com.halen.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_goodstype")
public class GoodsType {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 100)
    private String icon;

    @Column(length = 50)
    private String name;

    private Integer pId;

    private Integer state; // 1 root, 0 leaf
}
