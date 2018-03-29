package com.halen.controllers.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.halen.entity.DamageList;
import com.halen.entity.DamageListGoods;
import com.halen.entity.Log;
import com.halen.service.DamageListGoodsService;
import com.halen.service.DamageListService;
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
@RequestMapping("/admin/damageList")
public class DamageListAdminController {

    @Resource
    private DamageListService damageListService;

    @Resource
    private DamageListGoodsService damageListGoodsService;

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
    @RequiresPermissions(value = "商品报损")
    public String genCode() throws Exception {
        StringBuffer code = new StringBuffer();
        code.append(DateUtil.getCurrentDateStr());
        String damageNumber = damageListService.getTodayMaxDamageNumber();
        if (damageNumber != null) {
            code.append(StringUtil.formatCode(damageNumber));
        }else {
            code.append("0001");
        }
        return code.toString();
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "商品报损")
    public Map<String, Object> save(DamageList damageList, String goodsJson) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        damageList.setUser(userService.findByUsername((String) SecurityUtils.getSubject().getPrincipal()));
        Gson gson = new Gson();
        List<DamageListGoods> plgList = gson.fromJson(goodsJson, new TypeToken<List<DamageListGoods>>(){}.getType());
        damageListService.save(damageList, plgList);
        logService.save(new Log(Log.ADD_ACTION, "Add one damage list"));
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/list")
    @RequiresPermissions(value = "报损报溢查询")
    public Map<String, Object> list(DamageList damageList) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<DamageList> damageListList = damageListService.list(damageList, Sort.Direction.DESC, "damageDate");
        resultMap.put("rows", damageListList);
        return resultMap;
    }

    @RequestMapping("/listGoods")
    @RequiresPermissions(value = "报损报溢查询")
    public Map<String, Object> listGoods(Integer damageListId) throws Exception {
        if (damageListId == null) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", damageListGoodsService.listByDamageListId(damageListId));
        logService.save(new Log(Log.QUERY_ACTION, "Query damage list goods by damage list id"));
        return resultMap;
    }
}
