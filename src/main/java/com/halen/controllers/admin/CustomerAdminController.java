package com.halen.controllers.admin;

import com.halen.entity.Log;
import com.halen.entity.Customer;
import com.halen.service.LogService;
import com.halen.service.CustomerService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/customer")
public class CustomerAdminController {

    @Resource
    private CustomerService customerService;

    @Resource
    private LogService logService;

    @RequestMapping("/comboList")
    @RequiresPermissions(value = {"销售出库","客户退货","销售单据查询","客户退货查询"},logical= Logical.OR)
    public List<Customer> comboList(String query) throws Exception {
        if (query == null) {
            query = "";
        }
        return customerService.findByName("%" + query + "%");
    }

    @RequestMapping("/list")
    @RequiresPermissions(value = "客户管理")
    public Map<String, Object> list(Customer customer, @RequestParam(value = "page", required = false)Integer page, @RequestParam(value = "rows", required = false)Integer rows) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<Customer> customerList = customerService.list(customer, page, rows, Sort.Direction.ASC, "id");
        Long total = customerService.getCount(customer);
        resultMap.put("rows", customerList);
        resultMap.put("total", total);
        logService.save(new Log(Log.QUERY_ACTION, "Query customer information"));
        return resultMap;
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "客户管理")
    public Map<String, Object> save(Customer customer) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (customer.getId() != null) {
            logService.save(new Log(Log.UPDATE_ACTION, "Edit one customer: " + customer));
        }else {
            logService.save(new Log(Log.ADD_ACTION, "Add one customer: " + customer));
        }
        customerService.save(customer);
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = "客户管理")
    public Map<String, Object> delete(String ids) {
        Map<String, Object> resultMap = new HashMap<>();
        String []idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            int id = Integer.parseInt(idsStr[i]);
            logService.save(new Log(Log.DELETE_ACTION, "Delete one customer"));
            customerService.delete(id);
        }
        resultMap.put("success", true);
        return resultMap;
    }
}
