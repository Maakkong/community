package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/10 15:09
 * @Description No Description
 */
@Data
public class Comment {

    private Integer id;
    private Integer userId;
    private Integer entityType;
    private Integer entityId;
    private Integer targetId;
    private String content;
    private Integer status;
    private Date createTime;


}
