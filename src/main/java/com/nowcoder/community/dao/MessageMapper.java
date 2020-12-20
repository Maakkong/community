package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.List;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/17 14:03
 * @Description No Description
 */
@Mapper
public interface MessageMapper {

    /**
     * 查询当前用户会话列表,针对每个会话返回最新消息
     * @param userId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectConversations(@Param("userId") Integer userId,
                                      @Param("offset") Integer offset,
                                      @Param("limit") Integer limit);

    /**
     * 查询当前用户的会话数量
     * @param userId
     * @return
     */
    int selectConversasionCount(@Param("userId") Integer userId);

    /**
     * 查询会话消息
     * @param conversationId
     * @param offset
     * @param limit
     * @return
     */
    List<Message> selectLetters(@Param("conversationId") String conversationId,
                                @Param("offset") Integer offset, @Param("limit") Integer limit);

    /**
     * 查询会话消息数量
     * @param conversationId
     * @return
     */
    int selectLetterCount(@Param("conversationId") String conversationId);

    /**
     * 查询未读私信数量
     * @param userId
     * @param conversationId
     * @return
     */
    int selectLetterUnreadCount(@Param("userId") Integer userId,
                                @Param("conversationId") String conversationId);


}
