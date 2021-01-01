package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/22 15:02
 * @Description No Description
 */
public class CookieUtil {

    /**
     * 获取cookie值
     * @param request
     * @param name
     * @return
     */
    public static String getValue(HttpServletRequest request,String name){
        if(request==null||name==null){
            throw new IllegalArgumentException("参数不能为空！");
        }
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(name)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
