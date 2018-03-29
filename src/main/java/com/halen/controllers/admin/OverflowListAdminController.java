package com.halen.controllers.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.halen.entity.OverflowList;
import com.halen.entity.OverflowListGoods;
import com.halen.entity.Log;
import com.halen.service.OverflowListGoodsService;
import com.halen.service.OverflowListService;
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
@RequestMapping("/admin/overflowList")
public class OverflowListAdminController {

    @Resource
    private OverflowListService overflowListService;

    @Resource
    private OverflowListGoodsService overflowListGoodsService;

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
    @RequiresPermissions(value = "商品报溢")
    public String genCode() throws Exception {
        StringBuffer code = new StringBuffer();
        code.append(DateUtil.getCurrentDateStr());
        String overflowNumber = overflowListService.getTodayMaxOverflowNumber();
        if (overflowNumber != null) {
            code.append(StringUtil.formatCode(overflowNumber));
        }else {
            code.append("0001");
        }
        return code.toString();
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "商品报溢")
    public Map<String, Object> save(OverflowList overflowList, String goodsJson) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        overflowList.setUser(userService.findByUsername((String) SecurityUtils.getSubject().getPrincipal()));
        Gson gson = new Gson();
        List<OverflowListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<OverflowListGoods>>(){}.getType());
        overflowListService.save(overflowList, plgList);
        logService.save(new Log(Log.ADD_ACTION, "Add one Overflow list"));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/list")
    @RequiresPermissions(value = "报损报溢查询")
    public Map<String, Object> list(OverflowList overflowList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<OverflowList> overflowListList = overflowListService.list(overflowList, Sort.Direction.DESC, "overflowDate");
        resultMap.put("rows", overflowListList);
        return resultMap;
    }

    @RequestMapping("/listGoods")
    @RequiresPermissions(value = "报损报溢查询")
    public Map<String, Object> listGoods(Integer overflowListId) throws Exception {
        if (overflowListId == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", overflowListGoodsService.listByOverflowListId(overflowListId));
        logService.save(new Log(Log.QUERY_ACTION, "Query Overflow list goods by Overflow list id"));
        return resultMap;
    }
}
