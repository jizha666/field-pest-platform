package com.zhaji.controller;

import com.zhaji.pojo.LoginInfo;
import com.zhaji.pojo.Result;
import com.zhaji.pojo.User;
import com.zhaji.service.LoginService;
import com.zhaji.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        log.info("员工登录：{}", user);

        Boolean b = loginService.identityCheck(user);
        if (!b){
            return Result.error("用户身份不存在！");
        }

        User u = loginService.login(user);

        if (u != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", u.getId());
            claims.put("name", u.getName());
            claims.put("username", u.getUsername());
            claims.put("identity", u.getIdentity());

            String jwt = JwtUtils.generateJwt(claims);

            LoginInfo info = new LoginInfo(u.getId(), u.getUsername(), u.getName(), u.getIdentity(), jwt);
            return Result.success(info);
        }

        return Result.error("用户名或密码错误");
    }

    @GetMapping("/user")
    public Result user(String username) {
        log.info("用户信息查询，参数：{}", username);
//        Boolean b = loginService.usernameCheck(username);
//        if (b){
//            return Result.error("用户名已存在！");
//        }
        User result = loginService.queryUser(username);
        return Result.success(result);
    }

    @PutMapping("/user")
    public Result updateUser(@RequestBody User user) {
        log.info("更新数据：{}", user);
        Boolean b = loginService.usernameCheck(user);
        if (b) {
            return Result.error("用户名已存在！");
        }
        loginService.updateUser(user);
        return Result.success();
    }
}
