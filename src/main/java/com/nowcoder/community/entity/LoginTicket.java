package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/22 12:49
 * @Description No Description
 */
@Data
public class LoginTicket {

    private Integer id;
    private Integer userId;
    private String ticket;
    private Integer status;
    private Date expired;


}
