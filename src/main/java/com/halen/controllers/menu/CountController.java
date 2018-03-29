package com.halen.controllers.menu;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/count")
public class CountController {

    @GetMapping("/supplier")
    @RequiresPermissions(value = "供应商统计")
    public String saleOut() {
        return "count/supplier";
    }

    @GetMapping("/customer")
    @RequiresPermissions(value = "客户统计")
    public String customer() {
        return "count/customer";
    }

    @GetMapping("/purchase")
    @RequiresPermissions(value = "商品采购统计")
    public String purchase() {
        return "count/purchase";
    }

    @GetMapping("/saleDay")
    @RequiresPermissions(value = "按日统计分析")
    public String saleDay() {
        return "count/saleDay";
    }

    @GetMapping("/saleMonth")
    @RequiresPermissions(value = "按月统计分析")
    public String saleMonth() {
        return "count/saleMonth";
    }
}
