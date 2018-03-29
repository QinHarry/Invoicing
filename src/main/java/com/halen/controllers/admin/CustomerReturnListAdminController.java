package com.halen.controllers.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.halen.entity.CustomerReturnList;
import com.halen.entity.CustomerReturnListGoods;
import com.halen.entity.Log;
import com.halen.service.CustomerReturnListGoodsService;
import com.halen.service.CustomerReturnListService;
import com.halen.service.LogService;
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
@RequestMapping("/admin/customerReturnList")
public class CustomerReturnListAdminController {

    @Resource
    private CustomerReturnListService customerReturnListService;

    @Resource
    private CustomerReturnListGoodsService customerReturnListGoodsService;

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
    @RequiresPermissions(value = "客户退货")
    public String genCode() throws Exception {
        StringBuffer code = new StringBuffer();
        code.append(DateUtil.getCurrentDateStr());
        String customerReturnNumber = customerReturnListService.getTodayMaxCustomerReturnNumber();
        if (customerReturnNumber != null) {
            code.append(StringUtil.formatCode(customerReturnNumber));
        }else {
            code.append("0001");
        }
        return code.toString();
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "客户退货")
    public Map<String, Object> save(CustomerReturnList customerReturnList, String goodsJson) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        customerReturnList.setUser(userService.findByUsername((String) SecurityUtils.getSubject().getPrincipal()));
        Gson gson = new Gson();
        List<CustomerReturnListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<CustomerReturnListGoods>>(){}.getType());
        customerReturnListService.save(customerReturnList, plgList);
        logService.save(new Log(Log.ADD_ACTION, "Add one customer return list"));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/list")
    @RequiresPermissions(value = "客户退货查询")
    public Map<String, Object> list(CustomerReturnList customerReturnList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<CustomerReturnList> customerReturnListList = customerReturnListService.list(customerReturnList, Sort.Direction.DESC, "customerReturnDate");
        resultMap.put("rows", customerReturnListList);
        return resultMap;
    }

    @RequestMapping("/listCount")
    @RequiresPermissions(value="商品销售统计")
    public Map<String,Object> listCount(CustomerReturnList customerReturnList,CustomerReturnListGoods customerReturnListGoods)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        List<CustomerReturnList> customerReturnListList=customerReturnListService.list(customerReturnList, Sort.Direction.DESC, "customerReturnDate");
        for(CustomerReturnList pl:customerReturnListList){
            customerReturnListGoods.setCustomerReturnList(pl);
            List<CustomerReturnListGoods> plgList=customerReturnListGoodsService.list(customerReturnListGoods);
            pl.setCustomerReturnListGoodsList(plgList);
        }
        resultMap.put("rows", customerReturnListList);
        logService.save(new Log(Log.QUERY_ACTION,"Query Goods CustomerReturn record"));
        return resultMap;
    }

    @RequestMapping("/listGoods")
    @RequiresPermissions(value = "客户退货查询")
    public Map<String, Object> listGoods(Integer customerReturnListId) throws Exception {
        if (customerReturnListId == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", customerReturnListGoodsService.listByCustomerReturnListId(customerReturnListId));
        logService.save(new Log(Log.QUERY_ACTION, "Query customer return list goods by customer return list id"));
        return resultMap;
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = "客户退货查询")
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        customerReturnListService.delete(id);
        logService.save(new Log(Log.DELETE_ACTION, "Delete customer return list: " + customerReturnListService.findById(id)));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/update")
    @RequiresPermissions(value="客户统计")
    public Map<String,Object> update(Integer id)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        CustomerReturnList customerReturnList=customerReturnListService.findById(id);
        customerReturnList.setState(1);
        customerReturnListService.update(customerReturnList);
        resultMap.put("success", true);
        return resultMap;
    }
}
