package com.halen.controllers.admin;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.halen.entity.*;
import com.halen.service.*;
import com.halen.util.StringUtil;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/role")
public class RoleAdminController {

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private LogService logService;

    @RequestMapping("/listAll")
    @RequiresPermissions(value = {"用户管理", "角色管理"}, logical = Logical.OR)
    public Map<String, Object> listAll() throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("rows", roleService.listAll());
        logService.save(new Log(Log.QUERY_ACTION, "Search all roles information"));
        return resultMap;
    }

    @RequestMapping("/list")
    @RequiresPermissions(value = "角色管理")
    public Map<String, Object> list(Role role, @RequestParam(value = "page", required = false)Integer page, @RequestParam(value = "rows", required = false)Integer rows) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<Role> roleList = roleService.list(role, page, rows, Sort.Direction.ASC, "id");
        Long total  = roleService.getCount(role);
        resultMap.put("rows", roleList);
        resultMap.put("total", total);
        logService.save(new Log(Log.QUERY_ACTION, "Query roles pages"));
        return resultMap;
    }

    @RequestMapping("/save")
    @RequiresPermissions(value = "角色管理")
    public Map<String, Object> save(Role role) throws Exception {
        if (role.getId() != null) {
            logService.save(new Log(Log.UPDATE_ACTION, "Edit one role"));
        }else {
            logService.save(new Log(Log.ADD_ACTION, "Add one role"));
        }
        Map<String, Object> resultMap = new HashMap<>();
        if (role.getId() == null) {
            if (roleService.findByRoleName(role.getName()) != null) {
                resultMap.put("success", false);
                resultMap.put("errorInfo", "Role is already exist!");
                return resultMap;
            }
        }
        roleService.save(role);
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/delete")
    @RequiresPermissions(value = "角色管理")
    public Map<String, Object> delete(Integer id) throws Exception {
        logService.save(new Log(Log.DELETE_ACTION, "Delete one role"));
        Map<String, Object> resultMap = new HashMap<>();
        roleMenuService.deleteByRoleId(id);
        userRoleService.deleteByRoleId(id);
        roleService.delete(id);
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/loadCheckMenuInfo")
    @RequiresPermissions(value = "角色管理")
    public String loadCheckMenuInfo(Integer parentId, Integer roleId) throws Exception {
        List<Menu> menuList = menuService.findByRoleId(roleId);
        List<Integer> menuIdList = new LinkedList<>();
        for (Menu menu : menuList) {
            menuIdList.add(menu.getId());
        }
        return getAllCheckMenuByParentId(parentId, menuIdList).toString();
    }

    public JsonArray getAllCheckMenuByParentId(Integer parentId, List<Integer> menuIdList) {
        JsonArray jsonArray = this.getCheckMenuByParentId(parentId, menuIdList);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            if ("open".equals(jsonObject.get("state").getAsString())) {
                continue;
            }else {
                jsonObject.add("children", getAllCheckMenuByParentId(jsonObject.get("id").getAsInt(), menuIdList));
            }
        }
        return jsonArray;
    }

    public JsonArray getCheckMenuByParentId(Integer parentId, List<Integer> menuIdList) {
        List<Menu> menuList = menuService.findByParentId(parentId);
        JsonArray jsonArray = new JsonArray();
        for (Menu menu : menuList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", menu.getId());
            jsonObject.addProperty("text", menu.getName());
            if (menu.getState() == 1) {
                jsonObject.addProperty("state", "closed");
            }else {
                jsonObject.addProperty("state", "open");
            }
            jsonObject.addProperty("iconCls", menu.getIcon());
            if (menuIdList.contains(menu.getId())) {
                jsonObject.addProperty("checked", true);
            }
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    @RequestMapping("/saveMenuSet")
    @RequiresPermissions(value = "角色管理")
    public Map<String, Object> saveMenuSet(String menuIds, Integer roleId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        roleMenuService.deleteByRoleId(roleId);
        if (!StringUtil.isEmpty(menuIds)) {
            String idsStr[] = menuIds.split(",");
            for (int i = 0; i < idsStr.length; i++) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRole(roleService.findById(roleId));
                roleMenu.setMenu(menuService.findById(Integer.parseInt(idsStr[i])));
                roleMenuService.save(roleMenu);
            }
        }
        resultMap.put("success", true);
        return resultMap;
    }
}
