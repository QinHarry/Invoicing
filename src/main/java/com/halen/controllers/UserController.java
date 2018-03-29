package com.halen.controllers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.halen.entity.Log;
import com.halen.entity.Menu;
import com.halen.entity.Role;
import com.halen.entity.User;
import com.halen.service.LogService;
import com.halen.service.MenuService;
import com.halen.service.RoleService;
import com.halen.service.UserService;
import com.halen.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @Resource
    private LogService logService;

    @ResponseBody
    @RequestMapping("/login")
    public Map<String, Object> login(String imageCode, @Valid User user, BindingResult bindingResult, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isEmpty(imageCode)) {
            map.put("success", false);
            map.put("errorInfo", "Please input verify code");
            return map;
        }
        if (!session.getAttribute("checkcode").equals(imageCode)) {
            map.put("success", false);
            map.put("errorInfo", "verify is wrong");
            return map;
        }
        if (bindingResult.hasErrors()) {
            map.put("success", false);
            map.put("errorInfo", bindingResult.getFieldError().getDefaultMessage());
            return map;
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
            String username = (String) SecurityUtils.getSubject().getPrincipal();
            User currentUser = userService.findByUsername(username);
            session.setAttribute("currentUser", currentUser);
            List<Role> roleList = roleService.findByUserId(currentUser.getId());
            map.put("roleList", roleList);
            map.put("roleSize", roleList.size());
            map.put("success", true);
            logService.save(new Log(Log.LOGIN_ACTION, "User login"));
            return map;
        }catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
            map.put("errorInfo", "Username or password is incorrect");
            return map;
        }

    }

    @ResponseBody
    @RequestMapping("/saveRole")
    public Map<String, Object> saveRole(Integer roleId, HttpSession session) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Role currentRole = roleService.findById(roleId);
        session.setAttribute("currentRole", currentRole);
        map.put("success", true);
        return map;
    }

    @ResponseBody
    @GetMapping("/loadUserInfo")
    public String loadUserInfo(HttpSession session) throws Exception {
        User currentUser = (User) session.getAttribute("currentUser");
        Role currentRole = (Role) session.getAttribute("currentRole");
        return "Welcome: " + currentUser.getUsername() + "&nbsp;[&nbsp;" + currentRole.getName() + "&nbsp;]";
    }

    @ResponseBody
    @PostMapping("/loadMenuInfo")
    public String loadMenuInfo(HttpSession session, Integer parentId) throws Exception {
        Role currentRole = (Role) session.getAttribute("currentRole");
        return getAllMenuByParentId(parentId, currentRole.getId()).toString();
    }


    public JsonArray getAllMenuByParentId(Integer parentId, Integer roleId) {
        JsonArray jsonArray = this.getMenuByParentId(parentId, roleId);
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(i);
            if ("open".equals(jsonObject.get("state").getAsString())) {
                continue;
            }else {
                jsonObject.add("children", getAllMenuByParentId(jsonObject.get("id").getAsInt(), roleId));
            }
        }
        return jsonArray;
    }

    public JsonArray getMenuByParentId(Integer parentId, Integer roleId) {
        List<Menu> menuList = menuService.findByParentIdAndRoleId(parentId, roleId);
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
            JsonObject attributeObject = new JsonObject();
            attributeObject.addProperty("url", menu.getUrl());
            jsonObject.add("attributes", attributeObject);
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }
}
