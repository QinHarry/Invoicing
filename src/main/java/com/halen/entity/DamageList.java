package com.halen.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "t_damageList")
public class DamageList {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 100)
    private String damageNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private Date damageDate;

    @Transient
    private Date bDamageDate;

    @Transient
    private Date eDamageDate;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @Column(length = 1000)
    private String remarks;
}
