package com.nowcoder.community.service.impl;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/20 14:53
 * @Description No Description
 */
@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<Message> findConversations(Integer userId, Integer offset, Integer limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    @Override
    public int findConvesationCount(Integer userId) {
        return messageMapper.selectConversasionCount(userId);
    }

    @Override
    public List<Message> findLetters(String conversationId, Integer offset, Integer limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    @Override
    public int findLetterCount(String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    @Override
    public int findLetterUnreadCount(Integer userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    @Override
    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    @Override
    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids,1);
    }
}
