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
    List<Message> findLetters(String conversationId, Integer offset, Integer limit);

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

    /**
     * 添加消息
     * @param message
     * @return
     */
    int addMessage(Message message);

    /**
     * 消息状态变为已读
     * @param ids
     * @return
     */
    int readMessage(List<Integer> ids);

    /**
     * 查询最新的通知
     * @param userId
     * @param topic
     * @return
     */
    Message findLatestNotice(Integer userId,String topic);

    /**
     * 通知数
     * @param userId
     * @param topic
     * @return
     */
    int findNoticeCount(Integer userId,String topic);

    /**
     * 未读通知数
     * @param userId
     * @param topic
     * @return
     */
    int findNoticeUnreadCount(Integer userId,String topic);

    /**
     * 通知详情列表
     * @param userId
     * @param topic
     * @return
     */
    List<Message> findNotices(Integer userId,String topic,Integer offset,Integer limit);
}
