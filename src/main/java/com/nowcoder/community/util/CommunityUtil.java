package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/20 12:10
 * @Description No Description
 */
public class CommunityUtil {
    /**
     * 生成随机字符串
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    /**
     * MD加密
     * 传入已经加盐的字符串
     */
    public static String MD5(String key){
        if(StringUtils.isBlank(key)){
            return "";
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    /**
     * json封装
     * @param code
     * @param msg
     * @param map
     * @return
     */
    public static String getJSONString(int code, String msg, Map<String,Object> map){
        JSONObject json=new JSONObject();
        json.put("code",code);
        json.put("msg",msg);
        if(map!=null){
            for(String key:map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg){
        return getJSONString(code,msg,null);
    }

    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }

    public static void main(String[] args) {
        Map<String, Object> map=new HashMap<>();
        map.put("姓名","张三");
        System.out.println(getJSONString(200,"ok",map));
    }

}
