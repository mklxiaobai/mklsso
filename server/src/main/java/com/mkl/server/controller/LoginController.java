package com.mkl.server.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class LoginController {

    @Autowired
    StringRedisTemplate redisTemplate;
    @GetMapping("/login")
    public String login(@RequestParam(value = "redirec_url") String redirec_url,
                        @CookieValue(value = "user",required = false) String User,
                        HttpServletResponse response,
                        ModelMap model) throws IOException {
        //1、判断之前是否登录过
        if(!StringUtils.isEmpty(User)){
            //登录过,回到之前的地方，并且把当前ssoserver获取到的cookie以url方式传递给其他域名【cookie同步】
            String url = redirec_url+"?"+"user="+User;
            response.sendRedirect(url);
            return null;
        }else {
            //去redis验证，如果没有
            //没有登录过
            model.addAttribute("redirec_url",redirec_url);
            return "login";
        }
    }

    @PostMapping("/doLogin")
    public void doLogin(@RequestParam(value = "username") String username,@RequestParam(value = "password") String password, String redirec_url,
                        HttpServletResponse response,
                        HttpServletRequest request) throws IOException {
        //1、模拟用户的信息
        System.out.println(redirec_url);
        Map<String, Object> map = new HashMap<>();
        map.put("username",username);
        map.put("password",password);

        //2、以上标识用户登录; jwt
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(token, JSON.toJSONString(map));

        //3、登录成功。做两件事。
        //1）、命令浏览器把当前的token保存为cookie；  sso_user=token
        Cookie cookie = new Cookie("user",token);
        response.addCookie(cookie);
        response.sendRedirect(redirec_url+"?user="+token);

    }
}
