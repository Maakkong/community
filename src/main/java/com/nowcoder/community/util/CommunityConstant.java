package com.nowcoder.community.util;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/20 13:43
 * @Description No Description
 */
public interface CommunityConstant {

    /**
     * 激活成功
     */
    int ACTIVITY_SUCCESS=0;
    /**
     * 重复激活
     */
    int ACTIVITY_REPEAT=1;

    /**
     * 激活失败
     */
    int ACTIVITY_FAILURE=2;

    /**
     * 默认状态的登录凭证的超时时间
     */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住状态的登录凭证超时时间
     */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 10;

    /**
     * 实体类型： 帖子
     */
    int ENTITY_TYPE_POST=1;

    /**
     * 实体类型: 评论
     */
    int ENTITY_TYPE_Comment=2;

    /**
     * 实体类型: 用户
     */
    int ENTITY_TYPE_USER=3;
}
