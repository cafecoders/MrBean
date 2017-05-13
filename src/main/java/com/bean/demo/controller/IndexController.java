package com.bean.demo.controller;

import com.bean.demo.model.User;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by missinghigh on 2017/5/13.
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    @ResponseBody
    public String index(){
        return "Hello 2017/5/13";
    }

    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam("type") int type,
                          @RequestParam(value = "key", defaultValue = "zzz") String key
                          ){
        return String.format("Hello, %s / %d t:%d k:%s",groupId, userId, type, key);
    }

    @RequestMapping(path = {"/vm"}, method = {RequestMethod.GET})

    public String template(Model model){
        model.addAttribute("value1", "wv87");
        List<String> colors = Arrays.asList(new String[]{"Red", "Blue", "Yellow"});
        model.addAttribute("colors", colors);
        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < 4; i++)
            map.put(String.valueOf(i), String.valueOf(i*i));
        model.addAttribute("map", map);

        model.addAttribute("user", new User("LEE"));

        return "home";
    }

    @RequestMapping("/request")
    @ResponseBody
    public String request(Model model, HttpServletRequest httpServletRequest,
                          HttpServletResponse httpServletResponse, HttpSession httpSession,
                          @CookieValue("JSESSIONID") String sessionId
                          ){
        StringBuilder sb = new StringBuilder();
        sb.append("Cookie :"+ sessionId + "<br>");
        Enumeration<String> headNames = httpServletRequest.getHeaderNames();
        while(headNames.hasMoreElements()){
            String name = headNames.nextElement();
            sb.append(name + ":" + httpServletRequest.getHeader(name) + "<br>");
        }

        httpServletResponse.addCookie(new Cookie("newCoder", "panwansheng"));
        httpServletResponse.addHeader("newCoder", "pan");
        if(httpServletRequest.getCookies() != null){
            for(Cookie cookie : httpServletRequest.getCookies())
                sb.append(cookie.getName() + " " + cookie.getValue() );
        }

        sb.append(httpServletRequest.getMethod() + "<br>");
        sb.append(httpServletRequest.getQueryString() + "<br>");
        sb.append(httpServletRequest.getPathInfo() + "<br>");
        sb.append(httpServletRequest.getRequestURL() + "<br>");

        return sb.toString();
    }

    @RequestMapping(path = {"/redirect/{code}"})

    public String redirect(@PathVariable("code") int code){
        return "redirect:/";
    }

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key){
        if(key.equals("admin"))
            return "Welcome";

        throw new IllegalArgumentException("Wrong Parameters");
    }

    @ExceptionHandler
    @ResponseBody
    public String error(Exception e){
        return "error:" + e.getMessage();
    }
    //
}
