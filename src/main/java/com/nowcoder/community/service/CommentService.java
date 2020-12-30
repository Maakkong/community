package com.nowcoder.community.service;

import com.nowcoder.community.entity.Comment;

import java.util.List;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/10 15:25
 * @Description No Description
 */
public interface CommentService {

    /**
     * 按参数查询
     * @param entityType
     * @param entityId
     * @return
     */
    List<Comment> findCommentsByEntity(Integer entityType,Integer entityId,Integer offset,Integer limit);

    /**
     * 查询总条数
     * @param entityType
     * @param entityId
     * @return
     */
    int findCommentsCountByEntity(Integer entityType,Integer entityId);

    /**
     * 添加评论
     * @param comment
     * @return
     */
    int addComment(Comment comment);

    /**
     * 按id查询
     * @param id
     * @return
     */
    Comment findCommentById(Integer id);
}
