package com.nowcoder.community.dao.elasticsearch;


import com.nowcoder.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/31 17:18
 * @Description
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {


}
