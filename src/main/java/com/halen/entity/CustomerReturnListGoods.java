package com.halen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_customerReturnListGoods")
public class CustomerReturnListGoods {

    @Id
    @GeneratedValue
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "customerReturnListId")
    private CustomerReturnList customerReturnList;

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

    @Transient
    private String codeOrName;
}
