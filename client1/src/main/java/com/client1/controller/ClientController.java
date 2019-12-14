package com.client1.controller;

import com.client1.config.SsoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 马锴梁
 * @version 1.0
 * @date 2019/12/13 19:42
 */
@Controller
public class ClientController {
    @Autowired
    SsoConfig ssoConfig;
    /**
     *
     */
    @GetMapping("/")
    public String index(ModelMap modelMap, @CookieValue(value = "user",required = false) String userCookie, @RequestParam(value = "user",required = false) String userParam, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        if(!StringUtils.isEmpty(userParam)){
            Cookie user = new Cookie("user", userParam);
            httpServletResponse.addCookie(user);
            return "index";
        }
        //      如果未找到cookie则重定向至sso服务并传参本Url
        if(StringUtils.isEmpty(userCookie)){
            String url=ssoConfig.getUrl()+ssoConfig.getLoginPath()+"?redirec_url="+ httpServletRequest.getRequestURL().toString();
            httpServletResponse.sendRedirect(url);
            return null;
        }else {
            modelMap.addAttribute("User","天才");
            return "index";
        }
    }
}
