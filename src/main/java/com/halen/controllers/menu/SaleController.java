package com.halen.controllers.menu;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sale")
public class SaleController {

    @GetMapping("/saleout")
    @RequiresPermissions(value = "销售出库")
    public String saleOut() {
        return "sale/saleout";
    }

    @GetMapping("/saleSearch")
    @RequiresPermissions(value = "销售单据查询")
    public String saleSearch() {
        return "sale/saleSearch";
    }

    @GetMapping("/saleReturn")
    @RequiresPermissions(value = "客户退货")
    public String saleReturn() {
        return "sale/salereturn";
    }

    @GetMapping("/returnSearch")
    @RequiresPermissions(value = "退货单据查询")
    public String returnSearch() {
        return "sale/returnSearch";
    }
}
