package com.nowcoder.community.service.impl;

import com.nowcoder.community.dao.CommentMapper;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/10 15:25
 * @Description No Description
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;


    @Override
    public List<Comment> findCommentsByEntity(Integer entityType, Integer entityId,Integer offset,Integer limit) {
        return commentMapper.selectCommentByEntity(entityType,entityId,offset,limit);
    }

    @Override
    public int findCommentsCountByEntity(Integer entityType, Integer entityId) {
        return commentMapper.selectCountByEntity(entityType,entityId);
    }

}
