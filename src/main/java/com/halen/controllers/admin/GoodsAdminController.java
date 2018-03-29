package com.halen.controllers.admin;

import com.halen.entity.Goods;
import com.halen.entity.Log;
import com.halen.service.CustomerReturnListGoodsService;
import com.halen.service.GoodsService;
import com.halen.service.LogService;
import com.halen.service.SaleListGoodsService;
import com.halen.util.StringUtil;
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
@RequestMapping("/admin/goods")
public class GoodsAdminController {

    @Resource
    private GoodsService goodsService;

    @Resource
    private SaleListGoodsService saleListGoodsService;

    @Resource
    private CustomerReturnListGoodsService customerReturnListGoodsService;

    @Resource
    private LogService logService;

    @RequestMapping("/list")
    @RequiresPermissions(value = {"商品管理","进货入库","退货出库"},logical= Logical.OR)
    public Map<String, Object> list(Goods goods, @RequestParam(value = "page", required = false)Integer page, @RequestParam(value = "rows", required = false)Integer rows) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<Goods> goodsList = goodsService.list(goods, page, rows, Sort.Direction.ASC, "id");
        Long total = goodsService.getCount(goods);
        resultMap.put("rows", goodsList);
        resultMap.put("total", total);
        logService.save(new Log(Log.QUERY_ACTION, "Query goods information"));
        return resultMap;
    }

    @RequestMapping("/listAlarm")
    @RequiresPermissions(value="库存报警")
    public Map<String,Object> listAlarm() throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        resultMap.put("rows", goodsService.listAlarm());
        logService.save(new Log(Log.QUERY_ACTION,"Query alarm of inventory quantity"));
        return resultMap;
    }

    @RequestMapping("/listInventory")
    @RequiresPermissions(value = "当前库存查询")
    public Map<String, Object> listInventory(Goods goods, @RequestParam(value = "page", required = false)Integer page, @RequestParam(value = "rows", required = false)Integer rows) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<Goods> goodsList = goodsService.list(goods, page, rows, Sort.Direction.ASC, "id");
        for (Goods g : goodsList) {
            g.setSaleTotal(saleListGoodsService.getTotalByGoodsId(g.getId()) - customerReturnListGoodsService.getTotalByGoodsId(g.getId()));
        }
        Long total = goodsService.getCount(goods);
        resultMap.put("rows", goodsList);
        resultMap.put("total", total);
        logService.save(new Log(Log.QUERY_ACTION, "Query goods information"));
        return resultMap;
    }



    @RequestMapping("/listNoInventoryQuantity")
    @RequiresPermissions(value = "期初库存")
    public Map<String, Object> listNoInventoryQuantity(@RequestParam(value = "codeOrName", required = false)String codeOrName,
                                               @RequestParam(value = "page", required = false)Integer page,
                                               @RequestParam(value = "rows", required = false)Integer rows) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<Goods> goodsList = goodsService.listNoInventoryQuantityByCodeOrName(codeOrName, page, rows, Sort.Direction.ASC, "id");
        Long total = goodsService.getCountNoInventoryQuantityByCodeOrName(codeOrName);
        resultMap.put("rows", goodsList);
        resultMap.put("total", total);
        logService.save(new Log(Log.QUERY_ACTION, "Query goods which have no inventory quantity"));
        return resultMap;
    }

    @RequestMapping("/listHasInventoryQuantity")
    @RequiresPermissions(value = "期初库存")
    public Map<String, Object> listHasInventoryQuantity(@RequestParam(value = "codeOrName", required = false)String codeOrName,
                                                        @RequestParam(value = "page", required = false)Integer page,
                                                        @RequestParam(value = "rows", required = false)Integer rows) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<Goods> goodsList = goodsService.listHasInventoryQuantityByCodeOrName(codeOrName, page, rows, Sort.Direction.ASC, "id");
        Long total = goodsService.getCountHasInventoryQuantityByCodeOrName(codeOrName);
        resultMap.put("rows", goodsList);
        resultMap.put("total", total);
        logService.save(new Log(Log.QUERY_ACTION, "Query goods which have inventory quantity"));
        return resultMap;
    }


    @RequestMapping("/genGoodsCode")
    @RequiresPermissions(value = "商品管理")
    public String genGoodsCode() throws Exception {
        String maxGoodsCode = goodsService.getMaxGoodsCode();
        if (!StringUtil.isEmpty(maxGoodsCode)) {
            Integer code = Integer.parseInt(maxGoodsCode) + 1;
            String codes = code.toString();
            int length = codes.length();
            for (int i = 4; i > length; i--) {
                codes = "0" + codes;
            }
            return codes;
        }else {
            return "0001";
        }
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "商品管理")
    public Map<String, Object> save(Goods goods) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (goods.getId() != null) {
            logService.save(new Log(Log.UPDATE_ACTION, "Edit the goods: " + goods));
        }else {
            logService.save(new Log(Log.ADD_ACTION, "Add one goods: " + goods));
            goods.setLastPurchasingPrice(goods.getPurchasingPrice());
            goods.setState(0);
            goods.setInventoryQuantity(0);
        }
        goodsService.save(goods);
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = "商品管理")
    public Map<String, Object> delete(Integer id) throws Exception{
        Map<String, Object> resultMap = new HashMap<>();
        Goods goods = goodsService.findById(id);
        if (goods.getState() == 1) {
            resultMap.put("success", false);
            resultMap.put("errorInfo", "This goods cannot be deleted because it is already in initial inventory");
        }else if (goods.getState() == 2) {
            resultMap.put("success", false);
            resultMap.put("errorInfo", "This goods cannot be deleted because it already generated ticket");
        }else {
            logService.save(new Log(Log.DELETE_ACTION, "Delete one goods: " + goods));
            goodsService.delete(id);
            resultMap.put("success", true);
        }
        return resultMap;
    }

    @RequestMapping("/saveStore")
    @RequiresPermissions(value = "期初库存")
    public Map<String, Object> saveStore(Integer id, Integer num, Float price) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        Goods goods = goodsService.findById(id);
        goods.setInventoryQuantity(num);
        goods.setPurchasingPrice(price);
        goods.setLastPurchasingPrice(price);
        goodsService.save(goods);
        logService.save(new Log(Log.UPDATE_ACTION, "Edit goods with: " + goods + ", price=" + price + "inventory quantity=" + num));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/deleteStock")
    @RequiresPermissions(value = "期初库存")
    public Map<String, Object> deleteStock(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        Goods goods = goodsService.findById(id);
        if (goods.getState() == 2) {
            resultMap.put("success", false);
            resultMap.put("errorInfo", "This item cannot be deleted with tickets");
        }else {
            goods.setInventoryQuantity(0);
            goodsService.save(goods);
            logService.save(new Log(Log.UPDATE_ACTION, "Edit one goods: " + goods));
            resultMap.put("success", true);
        }
        return resultMap;
    }
}
