package com.halen.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "t_supplier")
public class Supplier {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(length = 200)
    private String name;

    @Column(length = 50)
    private String contact;

    @Column(length = 50)
    private String phone;

    @Column(length = 300)
    private String address;

    @Column(length = 1000)
    private String remarks;
}
