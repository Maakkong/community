package com.nowcoder.community;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/20 14:43
 * @Description No Description
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTest {

    @Autowired
    private MessageMapper messageMapper;
    @Test
    void testSelectLetters(){
        List<Message> messages = messageMapper.selectConversations(111, 0, 10);
        for (Message message : messages) {
            System.out.println(message);
        }
        System.out.println(messageMapper.selectConversasionCount(111));

        List<Message> letters = messageMapper.selectLetters("111_112", 0, 10);
        for (Message letter : letters) {
            System.out.println(letter);
        }
        System.out.println(messageMapper.selectLetterCount("111_112"));

        System.out.println(messageMapper.selectLetterUnreadCount(131,"111_131"));
    }
}
