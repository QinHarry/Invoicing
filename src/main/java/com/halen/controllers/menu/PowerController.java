package com.halen.controllers.menu;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/power")
public class PowerController {

    @GetMapping("/user")
    @RequiresPermissions(value = "用户管理")
    public String user() {
        return "power/user";
    }

    @GetMapping("/role")
    @RequiresPermissions(value = "角色管理")
    public String role() {
        return "power/role";
    }

    @GetMapping("/log")
    @RequiresPermissions(value = "系统日志")
    public String log() {
        return "power/log";
    }
}
