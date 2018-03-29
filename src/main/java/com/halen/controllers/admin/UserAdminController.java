package com.halen.controllers.admin;

import com.halen.entity.Log;
import com.halen.entity.Role;
import com.halen.entity.User;
import com.halen.entity.UserRole;
import com.halen.service.LogService;
import com.halen.service.RoleService;
import com.halen.service.UserRoleService;
import com.halen.service.UserService;
import com.halen.util.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/user")
public class UserAdminController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private LogService logService;

    @RequestMapping("/list")
    @ResponseBody
    @RequiresPermissions(value = "用户管理")
    public Map<String, Object> list(User user, @RequestParam(value = "page", required = false)Integer page, @RequestParam(value = "rows", required = false)Integer rows) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<User> userList = userService.list(user, page, rows, Sort.Direction.ASC, "id");
        for (User u : userList) {
            List<Role> roleList = roleService.findByUserId(u.getId());
            StringBuffer sb = new StringBuffer();
            for (Role r : roleList) {
                sb.append("," + r.getName());
            }
            u.setRoles(sb.toString().replaceFirst(",", ""));
        }
        Long total  = userService.getCount(user);
        resultMap.put("rows", userList);
        resultMap.put("total", total);
        logService.save(new Log(Log.QUERY_ACTION, "Query users with pages"));
        return resultMap;
    }

    @RequestMapping("/save")
    @ResponseBody
    @RequiresPermissions(value = "用户管理")
    public Map<String, Object> save(User user) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        if (user.getId() == null) {
            if (userService.findByUsername(user.getUsername()) != null) {
                resultMap.put("success", false);
                resultMap.put("errorInfo", "username is already exist!");
                return resultMap;
            }
        }
        if (user.getId() != null) {
            logService.save(new Log(Log.UPDATE_ACTION, "Edit one user"));
        }else {
            logService.save(new Log(Log.ADD_ACTION, "Add one user"));
        }
        userService.save(user);
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/delete")
    @ResponseBody
    @RequiresPermissions(value = "用户管理")
    public Map<String, Object> delete(Integer id) throws Exception {
        logService.save(new Log(Log.DELETE_ACTION, "Delete one user"));
        Map<String, Object> resultMap = new HashMap<>();
        userRoleService.deleteByUserId(id);
        userService.delete(id);
        resultMap.put("success", true);
        return resultMap;
    }

    @RequestMapping("/saveRoleSet")
    @ResponseBody
    @RequiresPermissions(value = "用户管理")
    public Map<String, Object> saveRoleSet(String roleIds, Integer userId) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        userRoleService.deleteByUserId(userId);
        if (!StringUtil.isEmpty(roleIds)) {
            String roleIdsStr[] = roleIds.split(",");
            for (int i = 0; i < roleIdsStr.length; i++) {
                UserRole userRole = new UserRole();
                userRole.setUser(userService.findById(userId));
                userRole.setRole(roleService.findById(Integer.parseInt(roleIdsStr[i])));
                userRoleService.save(userRole);
            }
        }
        resultMap.put("success", true);
        logService.save(new Log(Log.UPDATE_ACTION, "Save user's role setting"));
        return resultMap;
    }


    @RequestMapping("/modifyPassword")
    @ResponseBody
    @RequiresPermissions(value = "修改密码")
    public Map<String, Object> modifyPassword(String newPassword, HttpSession session) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        User currentUser = (User) session.getAttribute("currentUser");
        User user = userService.findById(currentUser.getId());
        user.setPassword(newPassword);
        userService.save(user);
        resultMap.put("success", true);
        logService.save(new Log(Log.UPDATE_ACTION, "Modify password"));
        return resultMap;
    }

    @GetMapping("/logout")
    @RequiresPermissions(value = "安全退出")
    public String logout(HttpSession session) throws Exception {
        logService.save(new Log(Log.LOGOUT_ACTION, "User logout"));
        SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }
}
