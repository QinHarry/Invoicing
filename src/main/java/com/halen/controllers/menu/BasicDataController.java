package com.halen.controllers.menu;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/basicData")
public class BasicDataController {

    @GetMapping("/supplier")
    @RequiresPermissions(value = "供应商管理")
    public String supplier() {
        return "basicData/supplier";
    }

    @GetMapping("/customer")
    @RequiresPermissions(value = "客户管理")
    public String customer() {
        return "basicData/customer";
    }

    @GetMapping("/goods")
    @RequiresPermissions(value = "商品管理")
    public String goods() {
        return "basicData/goods";
    }

    @GetMapping("/stock")
    @RequiresPermissions(value = "期初库存")
    public String stock() {
        return "basicData/stock";
    }
}
