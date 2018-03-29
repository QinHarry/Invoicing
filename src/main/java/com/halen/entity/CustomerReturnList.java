package com.halen.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "t_customerReturnList")
public class CustomerReturnList {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 100)
    private String customerReturnNumber;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    private Date customerReturnDate;

    @Transient
    private Date bCustomerReturnDate;

    @Transient
    private Date eCustomerReturnDate;

    private Float amountPayable;

    private Float amountPaid;

    private Integer state;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(length = 1000)
    private String remarks;

    @Transient
    private List<CustomerReturnListGoods> customerReturnListGoodsList = null;
}
