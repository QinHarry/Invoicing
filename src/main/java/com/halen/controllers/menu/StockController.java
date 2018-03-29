package com.halen.controllers.menu;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stock")
public class StockController {

    @GetMapping("/damage")
    @RequiresPermissions(value = "商品报损")
    public String damage() {
        return "stock/damage";
    }

    @GetMapping("/overflow")
    @RequiresPermissions(value = "商品报溢")
    public String overflow() {
        return "stock/overflow";
    }

    @GetMapping("/damageOverflowSearch")
    @RequiresPermissions(value = "报损报溢查询")
    public String damageOverflowSearch() {
        return "stock/damageOverflowSearch";
    }

    @GetMapping("/alarm")
    @RequiresPermissions(value = "库存报警")
    public String alarm() {
        return "stock/alarm";
    }
}
