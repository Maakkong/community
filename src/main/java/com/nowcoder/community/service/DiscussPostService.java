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
     * @return
     */
    List<DiscussPost> findDiscussionPosts(Integer userId,Integer offset,Integer limit);

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

    DiscussPost findDiscussPostById(Integer id);
}
