package com.nowcoder.community.service.impl;

import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/22 17:15
 * @Description No Description
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void like(Integer userId, Integer entityType, Integer entityId) {
        String redisKey= RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        Boolean isMember = redisTemplate.opsForSet().isMember(redisKey, userId);
        if(isMember){
            redisTemplate.opsForSet().remove(redisKey,userId);
        }else {
            redisTemplate.opsForSet().add(redisKey,userId);
        }
    }

    @Override
    public long findEntityLikeCount(Integer entityType, Integer entityId) {
        String redisKey= RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().size(redisKey);
    }

    @Override
    public int findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId) {
        String redisKey= RedisKeyUtil.getEntityLikeKey(entityType,entityId);
        return redisTemplate.opsForSet().isMember(redisKey,userId)?1:0;
    }
}
