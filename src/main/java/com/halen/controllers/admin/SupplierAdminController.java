package com.halen.controllers.admin;

import com.halen.entity.Log;
import com.halen.entity.Supplier;
import com.halen.service.LogService;
import com.halen.service.SupplierService;
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
@RequestMapping("/admin/supplier")
public class SupplierAdminController {

    @Resource
    private SupplierService supplierService;

    @Resource
    private LogService logService;

    @RequestMapping("/comboList")
    @RequiresPermissions(value = "进货入库")
    public List<Supplier> comboList(String query) throws Exception {
        if (query == null) {
            query = "";
        }
        return supplierService.findByName("%" + query + "%");
    }

    @RequestMapping("/list")
    @RequiresPermissions(value = "供应商管理")
    public Map<String, Object> list(Supplier supplier, @RequestParam(value = "page", required = false)Integer page, @RequestParam(value = "rows", required = false)Integer rows) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<Supplier> supplierList = supplierService.list(supplier, page, rows, Sort.Direction.ASC, "id");
        Long total = supplierService.getCount(supplier);
        resultMap.put("rows", supplierList);
        resultMap.put("total", total);
        logService.save(new Log(Log.QUERY_ACTION, "Query supplier information"));
        return resultMap;
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "供应商管理")
    public Map<String, Object> save(Supplier supplier) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (supplier.getId() != null) {
            logService.save(new Log(Log.UPDATE_ACTION, "Edit one supplier"));
        }else {
            logService.save(new Log(Log.ADD_ACTION, "Add one supplier"));
        }
        supplierService.save(supplier);
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = "供应商管理")
    public Map<String, Object> delete(String ids) {
        Map<String, Object> resultMap = new HashMap<>();
        String []idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            int id = Integer.parseInt(idsStr[i]);
            logService.save(new Log(Log.DELETE_ACTION, "Delete one supplier"));
            supplierService.delete(id);
        }
        resultMap.put("success", true);
        return resultMap;
    }
}
