package com.halen.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "t_saleList")
public class SaleList {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 100)
    private String saleNumber;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date saleDate;

    @Transient
    private Date bSaleDate;

    @Transient
    private Date eSaleDate;

    private Float amountPayable;

    private Float amountPaid;

    private Integer state;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(length = 1000)
    private String remarks;

    @Transient
    private List<SaleListGoods> saleListGoodsList = null;
}
