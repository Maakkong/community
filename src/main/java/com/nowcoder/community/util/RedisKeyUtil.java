package com.nowcoder.community.util;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/22 17:09
 * @Description redisKey生成工具类
 */
public class RedisKeyUtil {

    private static final String SPLIT=":";

    private static final String PREFIX_ENTITY_LIKE="like:entity";

    private static final String PREFIX_USER_LIKE="like:user";

    /**
     * 生成某个实体的赞
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return like:entity:entityType:entityId -> set(userId)
     */
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }

    /**
     * 某个用户的赞
     * @param userId
     * @return like:user:userId ->integer
     */
    public static String getUserLikeKey(Integer userId){
        return PREFIX_USER_LIKE+SPLIT+userId;
    }

}