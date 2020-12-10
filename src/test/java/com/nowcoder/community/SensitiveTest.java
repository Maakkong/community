package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/2 14:40
 * @Description No Description
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    void testSensitive(){
        String text="311聚众赌博,集体被捕";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
        text="@赌@博@";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
