package com.nowcoder.community.service;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/23 15:35
 * @Description No Description
 */
public interface FollowService {

    /**
     * 关注
     * @param userId
     * @param entityType
     * @param entityId
     */
    void follow(Integer userId,Integer entityType,Integer entityId);

    /**
     * 取消关注
     * @param userId
     * @param entityType
     * @param entityId
     */
    void unfollow(Integer userId,Integer entityType,Integer entityId);

    /**
     * 关注的实体数量
     * @param userId
     * @param entityType
     * @return
     */
    long findFolloweeCount(Integer userId,Integer entityType);

    /**
     * 实体的关注者的数量
     * @param entityType
     * @param entityId
     * @return
     */
    long findFollowerCount(Integer entityType,Integer entityId);

    /**
     * 用户是否关注该实体
     * @param userId
     * @param entityType
     * @param entityId
     * @return
     */
    boolean hasFollowed(Integer userId,Integer entityType,Integer entityId);
}
