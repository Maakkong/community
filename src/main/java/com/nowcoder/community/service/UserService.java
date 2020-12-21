package com.nowcoder.community.service;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;

import java.util.Map;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/17 16:23
 */
public interface UserService {
    /**
     * 按id查询user
     * @return
     */
    User findUserById(Integer id);

    /**
     * 注册
     * @return
     */
    Map<String, Object> register(User user);
    /**
     * 激活
     */
    int activation(Integer userId,String code);
    /**
     * 登录
     */
    Map<String, Object> login(String username,String password,int expiredSeconds);
    /**
     * 退出，凭证失效
     */
    void logout(String ticket);
    /**
     * 查询凭证
     */
    LoginTicket findLoginTicket(String ticket);

    /**
     * 更新头像
     */
    int updateHeader(Integer userId,String headerUrl);

    /**
     * 按名字查找
     * @param username
     * @return
     */
    User findUserByName(String username);
}
