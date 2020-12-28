package com.nowcoder.community.service;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/22 17:15
 * @Description No Description
 */
public interface LikeService {

    /**
     * 点赞
     * @param userId
     * @param entityType
     * @param entityId
     */
    void like(Integer userId,Integer entityType,Integer entityId,Integer entityUserId);

    /**
     * 查询实体的点赞数量
     * @param entityType
     * @param entityId
     * @return
     */
    long findEntityLikeCount(Integer entityType,Integer entityId);

    /**
     * 某人是否点过赞
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    int findEntityLikeStatus(Integer userId,Integer entityType,Integer entityId);

    /**
     * 某人获得的赞的数量
     * @param userId
     * @return
     */
    int findUserLikeCount(Integer userId);

}
