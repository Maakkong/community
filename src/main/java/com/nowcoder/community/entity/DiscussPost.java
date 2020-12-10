package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/17 15:55
 */
@Data
public class DiscussPost {

    private Integer id;

    private Integer userId;

    private String title;

    private String content;

    private Integer type;

    private Integer status;

    private Date createTime;

    private Integer commentCount;

    private Double score;
}
