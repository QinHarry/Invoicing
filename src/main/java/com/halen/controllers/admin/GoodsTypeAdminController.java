package com.halen.controllers.admin;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.halen.entity.GoodsType;
import com.halen.entity.Log;
import com.halen.service.GoodsService;
import com.halen.service.GoodsTypeService;
import com.halen.service.LogService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/goodsType")
public class GoodsTypeAdminController {

    @Resource
    private GoodsTypeService goodsTypeService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private LogService logService;

    @RequestMapping("/loadTreeInfo")
    @RequiresPermissions(value = {"商品管理","进货入库","退货出库","销售出库","客户退货","当前库存查询"},logical=Logical.OR)
    public String loadTreeInfo() throws Exception {
        logService.save(new Log(Log.QUERY_ACTION, "Query all goods types"));
        return getAllByParentId(-1).toString();
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = {"商品管理","进货入库","退货出库","销售出库","客户退货"},logical= Logical.OR)
    public Map<String, Object> save(String name, Integer parentId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        GoodsType goodsType = new GoodsType();
        goodsType.setName(name);
        goodsType.setPId(parentId);
        goodsType.setIcon("icon-folder");
        goodsType.setState(0);
        goodsTypeService.save(goodsType);

        GoodsType parentGoodsType = goodsTypeService.findById(parentId);
        parentGoodsType.setState(1);
        goodsTypeService.save(parentGoodsType);

        logService.save(new Log(Log.ADD_ACTION, "Add new one goods type:" + goodsType));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = {"商品管理","进货入库","退货出库","销售出库","客户退货"},logical=Logical.OR)
    public Map<String, Object> delete(Integer id) throws Exception {
        System.out.println(id);
        Map<String, Object> resultMap = new HashMap<>();
        if (goodsService.findByTypeId(id).size() == 0) {
            GoodsType goodsType = goodsTypeService.findById(id);
            if (goodsTypeService.findByParentId(goodsType.getPId()).size() == 1) {
                GoodsType parentGoodsType = goodsTypeService.findById(goodsType.getPId());
                parentGoodsType.setState(0);
                goodsTypeService.save(parentGoodsType);
            }
            logService.save(new Log(Log.DELETE_ACTION, "Delete goods type" + goodsType));
            goodsTypeService.delete(id);
            resultMap.put("success", true);
        }else {
            resultMap.put("success", false);
            resultMap.put("errorInfo", "There are some goods upon this type, it canot be deleted");
        }
        return resultMap;
    }

    public JsonArray getAllByParentId(Integer parentId) {
        JsonArray jsonArray = this.getByParentId(parentId);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            if ("open".equals(jsonObject.get("state").toString())) {
                continue;
            }else {
                jsonObject.add("children", getAllByParentId(jsonObject.get("id").getAsInt()));
            }
        }
        return jsonArray;

    }


    public JsonArray getByParentId(Integer parentId) {
        List<GoodsType> goodsTypeList = goodsTypeService.findByParentId(parentId);
        JsonArray jsonArray = new JsonArray();
        for (GoodsType goodsType : goodsTypeList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", goodsType.getId());
            jsonObject.addProperty("text", goodsType.getName());
            if (goodsType.getState() == 1) {
                jsonObject.addProperty("state", "closed");
            }else {
                jsonObject.addProperty("state", "open");
            }
            jsonObject.addProperty("iconCls", goodsType.getIcon());
            JsonObject attributeObject = new JsonObject();
            attributeObject.addProperty("state", goodsType.getState());
            jsonObject.add("attributes", attributeObject);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
