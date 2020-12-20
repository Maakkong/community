package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/17 13:59
 * @Description No Description
 */
@Data
public class Message {
    private  Integer id;
    private Integer fromId;
    private  Integer toId;
    private String conversationId;
    private String content;
    private Integer status;
    private Date createTime;
}
