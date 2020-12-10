package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/16 19:22
 */
@Data
public class User {
    private Integer id;

    private String username;

    private String password;

    private String salt;

    private String email;
    /**
     * 0-普通用户; 1-超级管理员; 2-版主;
     */
    private Integer type;
    /**
     * 0-未激活; 1-已激活;
     */
    private Integer status;

    private String activationCode;

    private String headerUrl;

    private Date createTime;

}
