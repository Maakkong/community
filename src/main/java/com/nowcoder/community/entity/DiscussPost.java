package com.nowcoder.community.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/17 15:55
 */
@Data
@Document(indexName = "discusspost",type = "_doc",shards = 6,replicas = 3)
public class DiscussPost {

    @Id
    private Integer id;

    @Field(type= FieldType.Integer)
    private Integer userId;

    @Field(type= FieldType.Text,analyzer = "ik-max-word",searchAnalyzer = "ik-smart-word")
    private String title;

    @Field(type= FieldType.Text,analyzer = "ik-max-word",searchAnalyzer = "ik-smart-word")
    private String content;

    @Field(type= FieldType.Integer)
    private Integer type;

    @Field(type= FieldType.Integer)
    private Integer status;

    @Field(type= FieldType.Date)
    private Date createTime;

    @Field(type= FieldType.Integer)
    private Integer commentCount;

    @Field(type= FieldType.Double)
    private Double score;
}
