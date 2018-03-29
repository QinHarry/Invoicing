package com.halen.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "t_returnList")
public class ReturnList {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 100)
    private String returnNumber;

    @ManyToOne
    @JoinColumn(name = "supplierId")
    private Supplier supplier;

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnDate;

    @Transient
    private Date bReturnDate;

    @Transient
    private Date eReturnDate;

    private Float amountPayable;

    private Float amountPaid;

    private Integer state;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(length = 1000)
    private String remarks;

    @Transient
    private List<ReturnListGoods> returnListGoodsList = null;
}
