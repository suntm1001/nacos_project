package com.stm.controller;

import com.alibaba.fastjson.JSONObject;
import com.stm.common.CodeMsg;
import com.stm.common.Result;
import com.stm.pojo.User;
import com.stm.service.TokenService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private TokenService tokenService;
    /**
     * 登录验证
     * @param user
     * @param response
     * @return
     */
    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    public Result<Object> login(@RequestBody User user, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();
        //获取用户账号密码
        User userForBase = new User();
        userForBase.setId("1");//userService.findByUsername(user).getId()
        userForBase.setUsername("zhangsan");//userService.findByUsername(user).getUsername()
        userForBase.setPassword("123456");//userService.findByUsername(user).getPassword()
        //判断账号或密码是否正确
        if (!userForBase.getPassword().equals(user.getPassword())) {
            return Result.error(CodeMsg.USER_OR_PASS_ERROR);
        } else {
            String token = tokenService.getToken(userForBase);
            jsonObject.put("token", token);
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            response.addCookie(cookie);
            return Result.success(jsonObject);
        }
    }
}
