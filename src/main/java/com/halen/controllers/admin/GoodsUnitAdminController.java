package com.halen.controllers.admin;

import com.halen.entity.GoodsUnit;
import com.halen.entity.Log;
import com.halen.service.GoodsUnitService;
import com.halen.service.LogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/goodsUnit")
public class GoodsUnitAdminController {

    @Resource
    private GoodsUnitService goodsUnitService;

    @Resource
    private LogService logService;

    @RequestMapping("/comboList")
    @RequiresPermissions(value = "商品管理")
    public List<GoodsUnit> comboList() throws Exception {
        return goodsUnitService.listAll();
    }

    @RequestMapping("/listAll")
    @RequiresPermissions(value = "商品管理")
    public Map<String, Object> listAll() throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", goodsUnitService.listAll());
        logService.save(new Log(Log.QUERY_ACTION, "Query all goods unit"));
        return resultMap;
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "商品管理")
    public Map<String, Object> save(GoodsUnit goodsUnit) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        goodsUnitService.save(goodsUnit);
        logService.save(new Log(Log.ADD_ACTION, "Add one goods unit"));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = "商品管理")
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        logService.save(new Log(Log.DELETE_ACTION, "Delete one goods unit" + goodsUnitService.findById(id)));
        goodsUnitService.delete(id);
        resultMap.put("success", true);
        return resultMap;
    }
}
