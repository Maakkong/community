package com.nowcoder.community.service;

import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.domain.Page;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2021/1/1 15:22
 * @Description No Description
 */
public interface ElasticSearchService {

    /**
     * 增加帖子到ES
     * @param post
     */
    void saveDiscussPost(DiscussPost post);

    /**
     * 按id删除ES内文档
     * @param id
     */
    void deleteDiscussPost(Integer id);

    /**
     * 搜索帖子
     * @return
     */
    Page<DiscussPost> searchDiscussPost(String keyword,int current,int limit);
}
