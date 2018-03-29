package com.halen.controllers.menu;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchase")
public class PurchaseController {

    @GetMapping("/purchase")
    @RequiresPermissions(value = "进货入库")
    public String purchase() {
        return "purchase/purchase";
    }

    @GetMapping("/return")
    @RequiresPermissions(value = "退货出库")
    public String returnList() {
        return "purchase/return";
    }

    @GetMapping("/purchaseSearch")
    @RequiresPermissions(value = "进货单据查询")
    public String purchaseSearch() {
        return "purchase/purchaseSearch";
    }

    @GetMapping("/returnSearch")
    @RequiresPermissions(value = "退货单据查询")
    public String returnSearch() {
        return "purchase/returnSearch";
    }
}
