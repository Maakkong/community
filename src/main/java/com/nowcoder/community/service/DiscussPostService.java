package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;

import java.util.List;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/17 16:18
 */
public interface DiscussPostService {
    /**
     * 查询帖子
     * @param userId
     * @param offset
     * @param limit
     * @param orederMode
     * @return
     */
    List<DiscussPost> findDiscussionPosts(Integer userId,Integer offset,Integer limit,Integer orederMode);

    /**
     * 总行数
     * @param userId
     * @return
     */
    int findDiscussPostRows(Integer userId);

    /**
     * 添加
     * @param discussPost
     * @return
     */
    int addDiscussPost(DiscussPost discussPost);

    /**
     * 找帖子
     * @param id
     * @return
     */
    DiscussPost findDiscussPostById(Integer id);

    /**
     * 更新帖子id
     * @param id
     * @param commentCount
     * @return
     */
    int updateCommentCount(Integer id,Integer commentCount);

    /**
     * 更改帖子类型
     * @param id
     * @param type
     * @return
     */
    int updateType(Integer id,Integer type);

    /**
     * 更新帖子状态
     * @param id
     * @param status
     * @return
     */
    int updateStatus(Integer id,Integer status);

    int updateScore(int postId, double score);
}
