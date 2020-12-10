package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/19 12:54
 * @Description No Description
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    /**
     * 模板引擎核心类
     */
    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail(){
        mailClient.sendMail("1994135134@qq.com","testText","认真检查阿！大哥");
    }

    @Test
    public void testHTMLMail(){
        Context context=new Context();
        context.setVariable("username","superMan");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("1994135134@qq.com","testHTML",content);
    }
}
