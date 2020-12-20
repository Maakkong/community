package com.nowcoder.community.service;

import com.nowcoder.community.entity.Message;

import java.util.List;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/20 14:52
 * @Description No Description
 */
public interface MessageService {

    /**
     * 查询当前用户会话列表,针对每个会话返回最新消息
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> findConversations(Integer userId,Integer offset,Integer limit);

    /**
     * 用户会话数
     * @param userId
     * @return
     */
    int findConvesationCount(Integer userId);

    /**
     * 一个会话的全部私信
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> findLettes(String conversationId,Integer offset,Integer limit);

    /**
     * 一个会话的消息数
     * @param conversationId
     * @return
     */
    int findLetterCount(String conversationId);

    /**
     * 一个会话的未读消息数
     * @param userId
     * @param conversationId
     * @return
     */
    int findLetterUnreadCount(Integer userId,String conversationId);
}
