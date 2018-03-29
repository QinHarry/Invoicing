package com.halen.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "t_overflowList")
public class OverflowList {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 100)
    private String overflowNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private Date overflowDate;

    @Transient
    private Date bOverflowDate;

    @Transient
    private Date eOverflowDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(length = 1000)
    private String remarks;
}
