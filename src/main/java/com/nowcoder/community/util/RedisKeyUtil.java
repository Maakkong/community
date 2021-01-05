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

    private static final String PREFIX_FOLLOWEE="followee";

    private static final String PREFIX_FOLLOWER="follower";

    private static final String PREFIX_KAPTCHA="kapthcha";

    private static final String PREFIX_TICKET="ticket";

    private static final String PREFIX_USER="user";

    private static final String PREFIX_UV="uv";

    private static final String PREFIX_DAU="dau";

    private static final String PREFIX_POST="post";




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
     * 某个用户收到的赞
     * @param userId
     * @return like:user:userId ->integer
     */
    public static String getUserLikeKey(Integer userId){
        return PREFIX_USER_LIKE+SPLIT+userId;
    }

    /**
     * 某个用户关注的目标
     * @param userId
     * @param entityType
     * @return followee:userId:entityType -> zset(entityId,now)
     */
    public static String getFolloweeKey(Integer userId,Integer entityType){
        return PREFIX_FOLLOWEE+SPLIT+userId+SPLIT+entityType;
    }

    /**
     * 某个用户拥有的粉丝
     * @param entityType
     * @param entityId
     * @return follower:entityType:entityId -> zset(userId,now)
     */
    public static String getFollowerKey(Integer entityType,Integer entityId){
        return PREFIX_FOLLOWER+SPLIT+entityType+SPLIT+entityId;
    }

    /**
     * 验证码
     * @param owner
     * @return
     */
    public static String getKaptchaKey(String owner){
        return PREFIX_KAPTCHA+SPLIT+owner;
    }

    /**
     * 登录凭证
     * @param ticket
     * @return
     * tickt:string
     */
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET+SPLIT+ticket;
    }

    /**
     * 缓存中暂存user
     * @param userId
     * @return
     * user:userId
     */
    public static String getUserKey(Integer userId){
        return PREFIX_USER+SPLIT+userId;
    }

    /**
     * 单日独立访客
     * @param date
     * @return
     */
    public static String getUVKey(String date){
        return PREFIX_UV+SPLIT+date;
    }

    /**
     * 区间UV
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getUVKey(String startDate,String endDate){
        return PREFIX_UV+SPLIT+startDate+SPLIT+endDate;
    }

    /**
     * 单日活跃用户
     * @param date
     * @return
     */
    public static String getDAUKey(String date){
        return PREFIX_DAU+SPLIT+date;
    }

    /**
     * 区间日活跃用户
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getDAUKey(String startDate,String endDate){
        return PREFIX_DAU+SPLIT+startDate+SPLIT+endDate;
    }

    /**
     * 帖子分数key
     * @return
     */
    public static String getPostScoreKey(){
        return PREFIX_POST+SPLIT+"score";
    }
}
