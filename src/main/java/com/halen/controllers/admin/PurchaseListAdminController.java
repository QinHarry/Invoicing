package com.halen.controllers.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.halen.entity.Log;
import com.halen.entity.PurchaseList;
import com.halen.entity.PurchaseListGoods;
import com.halen.service.LogService;
import com.halen.service.PurchaseListGoodsService;
import com.halen.service.PurchaseListService;
import com.halen.service.UserService;
import com.halen.util.DateUtil;
import com.halen.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/purchaseList")
public class PurchaseListAdminController {

    @Resource
    private PurchaseListService purchaseListService;

    @Resource
    private PurchaseListGoodsService purchaseListGoodsService;

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    @InitBinder
    public void initBinber(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @RequestMapping("/genCode")
    @RequiresPermissions(value = "进货入库")
    public String genCode() throws Exception {
        StringBuffer code = new StringBuffer();
        code.append(DateUtil.getCurrentDateStr());
        String purchaseNumber = purchaseListService.getTodayMaxPurchaseNumber();
        if (purchaseNumber != null) {
            code.append(StringUtil.formatCode(purchaseNumber));
        }else {
            code.append("0001");
        }
        return code.toString();
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "进货入库")
    public Map<String, Object> save(PurchaseList purchaseList, String goodsJson) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        purchaseList.setUser(userService.findByUsername((String) SecurityUtils.getSubject().getPrincipal()));
        Gson gson = new Gson();
        List<PurchaseListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<PurchaseListGoods>>(){}.getType());
        purchaseListService.save(purchaseList, plgList);
        logService.save(new Log(Log.ADD_ACTION, "Add one purchase list"));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/list")
    @RequiresPermissions(value = "进货单据查询")
    public Map<String, Object> list(PurchaseList purchaseList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<PurchaseList> purchaseListList = purchaseListService.list(purchaseList, Sort.Direction.DESC, "purchaseDate");
        resultMap.put("rows", purchaseListList);
        return resultMap;
    }

    @RequestMapping("/listCount")
    @RequiresPermissions(value="商品采购统计")
    public Map<String,Object> listCount(PurchaseList purchaseList,PurchaseListGoods purchaseListGoods)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        List<PurchaseList> purchaseListList=purchaseListService.list(purchaseList, Sort.Direction.DESC, "purchaseDate");
        for(PurchaseList pl:purchaseListList){
            purchaseListGoods.setPurchaseList(pl);
            List<PurchaseListGoods> plgList=purchaseListGoodsService.list(purchaseListGoods);
            pl.setPurchaseListGoodsList(plgList);
        }
        resultMap.put("rows", purchaseListList);
        logService.save(new Log(Log.QUERY_ACTION,"Query Goods Purchase record"));
        return resultMap;
    }

    @RequestMapping("/listGoods")
    @RequiresPermissions(value = "进货单据查询")
    public Map<String, Object> listGoods(Integer purchaseListId) throws Exception {
        if (purchaseListId == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", purchaseListGoodsService.listByPurchaseListId(purchaseListId));
        logService.save(new Log(Log.QUERY_ACTION, "Query purchase list goods by purchase list id"));
        return resultMap;
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = "进货单据查询")
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        purchaseListService.delete(id);
        logService.save(new Log(Log.DELETE_ACTION, "Delete purchase list" + purchaseListService.findById(id)));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/update")
    @RequiresPermissions(value = "供应商统计")
    public Map<String, Object> update(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        PurchaseList purchaseList = purchaseListService.findById(id);
        purchaseList.setState(1);
        purchaseListService.update(purchaseList);
        resultMap.put("success", true);
        return resultMap;
    }
}
