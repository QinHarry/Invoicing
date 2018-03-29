package com.halen.controllers.admin;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.halen.entity.Log;
import com.halen.entity.SaleCount;
import com.halen.entity.SaleList;
import com.halen.entity.SaleListGoods;
import com.halen.service.LogService;
import com.halen.service.SaleListGoodsService;
import com.halen.service.SaleListService;
import com.halen.service.UserService;
import com.halen.util.DateUtil;
import com.halen.util.MathUtil;
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
import java.util.*;

@RestController
@RequestMapping("/admin/saleList")
public class SaleListAdminController {

    @Resource
    private SaleListService saleListService;

    @Resource
    private SaleListGoodsService saleListGoodsService;

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
    @RequiresPermissions(value = "销售出库")
    public String genCode() throws Exception {
        StringBuffer code = new StringBuffer();
        code.append(DateUtil.getCurrentDateStr());
        String saleNumber = saleListService.getTodayMaxSaleNumber();
        if (saleNumber != null) {
            code.append(StringUtil.formatCode(saleNumber));
        }else {
            code.append("0001");
        }
        return code.toString();
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "销售出库")
    public Map<String, Object> save(SaleList saleList, String goodsJson) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        saleList.setUser(userService.findByUsername((String) SecurityUtils.getSubject().getPrincipal()));
        Gson gson = new Gson();
        List<SaleListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<SaleListGoods>>(){}.getType());
        saleListService.save(saleList, plgList);
        logService.save(new Log(Log.ADD_ACTION, "Add one sale out list"));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/list")
    @RequiresPermissions(value = "退货单据查询")
    public Map<String, Object> list(SaleList saleList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<SaleList> saleListList = saleListService.list(saleList, Sort.Direction.DESC, "saleDate");
        resultMap.put("rows", saleListList);
        return resultMap;
    }

    @RequestMapping("/listCount")
    @RequiresPermissions(value="商品销售统计")
    public Map<String,Object> listCount(SaleList saleList,SaleListGoods saleListGoods)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        List<SaleList> saleListList=saleListService.list(saleList, Sort.Direction.DESC, "saleDate");
        for(SaleList pl:saleListList){
            saleListGoods.setSaleList(pl);
            List<SaleListGoods> plgList=saleListGoodsService.list(saleListGoods);
            pl.setSaleListGoodsList(plgList);
        }
        resultMap.put("rows", saleListList);
        logService.save(new Log(Log.QUERY_ACTION,"Query Goods Sale record"));
        return resultMap;
    }

    @RequestMapping("/listGoods")
    @RequiresPermissions(value = "退货单据查询")
    public Map<String, Object> listGoods(Integer saleListId) throws Exception {
        if (saleListId == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", saleListGoodsService.listBySaleListId(saleListId));
        logService.save(new Log(Log.QUERY_ACTION, "Query sale list goods by sale list id"));
        return resultMap;
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = "退货单据查询")
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        saleListService.delete(id);
        logService.save(new Log(Log.DELETE_ACTION, "Delete sale list" + saleListService.findById(id)));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/update")
    @RequiresPermissions(value="客户统计")
    public Map<String,Object> update(Integer id)throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        SaleList saleList=saleListService.findById(id);
        saleList.setState(1);
        saleListService.update(saleList);
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/countSaleByDay")
    @RequiresPermissions(value="按日统计分析")
    public Map<String,Object> countSaleByDay(String begin, String end) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        List<SaleCount> scList = new ArrayList<>();
        List<String> dates = DateUtil.getRangeDates(begin, end);
        List<Object> ll = saleListService.countSaleByDay(begin, end);
        for (String date: dates) {
            SaleCount sc = new SaleCount();
            sc.setDate(date);
            boolean flag = false;
            for (Object o: ll) {
                Object []oo = (Object[]) o;
                String dd = oo[2].toString().substring(0, 10);
                if (dd.equals(date)) {
                    sc.setAmountCost(MathUtil.format2Bit(Float.parseFloat(oo[0].toString())));
                    sc.setAmountSale(MathUtil.format2Bit(Float.parseFloat(oo[1].toString())));
                    sc.setAmountProfit(MathUtil.format2Bit(sc.getAmountSale() - sc.getAmountCost()));
                    flag = true;
                }
            }
            if (!flag) {
                sc.setAmountCost(0);
                sc.setAmountSale(0);
                sc.setAmountProfit(0);
            }
            scList.add(sc);
        }
        resultMap.put("rows", scList);
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/countSaleByMonth")
    @RequiresPermissions(value="按月统计分析")
    public Map<String,Object> countSaleByMonth(String begin, String end) throws Exception {
        Map<String,Object> resultMap = new HashMap<>();
        List<SaleCount> scList = new ArrayList<>();
        List<String> months = DateUtil.getRangeMonths(begin, end);
        List<Object> ll = saleListService.countSaleByMonth(begin, end);
        for (String month: months) {
            SaleCount sc = new SaleCount();
            sc.setDate(month);
            boolean flag = false;
            for (Object o: ll) {
                Object []oo = (Object[]) o;
                String dd = oo[2].toString().substring(0, 10);
                if (dd.equals(month)) {
                    sc.setAmountCost(MathUtil.format2Bit(Float.parseFloat(oo[0].toString())));
                    sc.setAmountSale(MathUtil.format2Bit(Float.parseFloat(oo[1].toString())));
                    sc.setAmountProfit(MathUtil.format2Bit(sc.getAmountSale() - sc.getAmountCost()));
                    flag = true;
                }
            }
            if (!flag) {
                sc.setAmountCost(0);
                sc.setAmountSale(0);
                sc.setAmountProfit(0);
            }
            scList.add(sc);
        }
        resultMap.put("rows", scList);
        resultMap.put("success", true);
        return resultMap;
    }
}
