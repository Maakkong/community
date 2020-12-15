package com.nowcoder.community;

import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/10 14:53
 * @Description No Description
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TranscationTest {

    @Autowired
    private AlphaService alphaService;

    @Test
    void testSave1(){
        Object obj=alphaService.save1();
        System.out.println(obj);
    }

    @Test
    void testSave2(){
        Object obj=alphaService.save2();
        System.out.println(obj);
    }

}
