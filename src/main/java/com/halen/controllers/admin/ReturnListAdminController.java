package com.halen.controllers.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.halen.entity.Log;
import com.halen.entity.ReturnList;
import com.halen.entity.ReturnListGoods;
import com.halen.service.LogService;
import com.halen.service.ReturnListGoodsService;
import com.halen.service.ReturnListService;
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
@RequestMapping("/admin/returnList")
public class ReturnListAdminController {

    @Resource
    private ReturnListService returnListService;

    @Resource
    private ReturnListGoodsService returnListGoodsService;

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
    @RequiresPermissions(value = "退货出库")
    public String genCode() throws Exception {
        StringBuffer code = new StringBuffer();
        code.append(DateUtil.getCurrentDateStr());
        String returnNumber = returnListService.getTodayMaxReturnNumber();
        if (returnNumber != null) {
            code.append(StringUtil.formatCode(returnNumber));
        }else {
            code.append("0001");
        }
        return code.toString();
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "退货出库")
    public Map<String, Object> save(ReturnList returnList, String goodsJson) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        returnList.setUser(userService.findByUsername((String) SecurityUtils.getSubject().getPrincipal()));
        Gson gson = new Gson();
        List<ReturnListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<ReturnListGoods>>(){}.getType());
        returnListService.save(returnList, plgList);
        logService.save(new Log(Log.ADD_ACTION, "Add one return list"));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/list")
    @RequiresPermissions(value = "退货单据查询")
    public Map<String, Object> list(ReturnList returnList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<ReturnList> returnListList = returnListService.list(returnList, Sort.Direction.DESC, "returnDate");
        resultMap.put("rows", returnListList);
        return resultMap;
    }

    @RequestMapping("/listCount")
    @RequiresPermissions(value="商品采购统计")
    public Map<String,Object> listCount(ReturnList returnList,ReturnListGoods returnListGoods)throws Exception{
        Map<String,Object> resultMap=new HashMap<>();
        List<ReturnList> returnListList=returnListService.list(returnList, Sort.Direction.DESC, "returnDate");
        for(ReturnList rl:returnListList){
            returnListGoods.setReturnList(rl);
            List<ReturnListGoods> rlgList=returnListGoodsService.list(returnListGoods);
            rl.setReturnListGoodsList(rlgList);
        }
        resultMap.put("rows", returnListList);
        logService.save(new Log(Log.QUERY_ACTION,"Query Goods Purchase record"));
        return resultMap;
    }

    @RequestMapping("/listGoods")
    @RequiresPermissions(value = "退货单据查询")
    public Map<String, Object> listGoods(Integer returnListId) throws Exception {
        if (returnListId == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", returnListGoodsService.listByReturnListId(returnListId));
        logService.save(new Log(Log.QUERY_ACTION, "Query return list goods by return list id"));
        return resultMap;
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = "退货单据查询")
    public Map<String, Object> delete(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        returnListService.delete(id);
        logService.save(new Log(Log.DELETE_ACTION, "Delete return list" + returnListService.findById(id)));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/update")
    @RequiresPermissions(value = "供应商统计")
    public Map<String, Object> update(Integer id) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        ReturnList returnList = returnListService.findById(id);
        returnList.setState(1);
        returnListService.update(returnList);
        resultMap.put("success", true);
        return resultMap;
    }
}
