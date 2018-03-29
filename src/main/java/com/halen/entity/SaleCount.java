package com.halen.entity;

import lombok.Data;

@Data
public class SaleCount {

    private float amountCost;
    private float amountSale;
    private float amountProfit;

    private String date;
}
