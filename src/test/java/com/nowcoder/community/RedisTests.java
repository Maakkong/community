package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.TimeUnit;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/22 16:30
 * @Description No Description
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class RedisTests {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void testString(){
        String redisKey="test:count";
        redisTemplate.opsForValue().set(redisKey,1);
        System.out.println(redisTemplate.opsForValue().get(redisKey));
        System.out.println(redisTemplate.opsForValue().increment(redisKey));
        System.out.println(redisTemplate.opsForValue().decrement(redisKey));
    }

    @Test
    void testHash(){
        String redisKey="test:user";
        redisTemplate.opsForHash().put(redisKey,"id",1);
        redisTemplate.opsForHash().put(redisKey,"username","mc");
        System.out.println(redisTemplate.opsForHash().get(redisKey,"id"));
        System.out.println(redisTemplate.opsForHash().get(redisKey,"username"));
    }

    @Test
    void testList(){
        String redisKey="test:ids";
        redisTemplate.opsForList().leftPush(redisKey,101);
        redisTemplate.opsForList().leftPush(redisKey,102);
        redisTemplate.opsForList().leftPush(redisKey,103);
        System.out.println(redisTemplate.opsForList().size(redisKey));
        System.out.println(redisTemplate.opsForList().index(redisKey,0));
        System.out.println(redisTemplate.opsForList().range(redisKey,0,2));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
        System.out.println(redisTemplate.opsForList().leftPop(redisKey));
    }

    @Test
    void testSet(){
        String redisKey="test:teachers";
        redisTemplate.opsForSet().add(redisKey,"aaa","bbb","ccc","ddd","eee");
        System.out.println(redisTemplate.opsForSet().size(redisKey));
        System.out.println(redisTemplate.opsForSet().pop(redisKey));
        System.out.println(redisTemplate.opsForSet().members(redisKey));
    }

    @Test
    void testZSet(){
        String redisKey="test:students";
        redisTemplate.opsForZSet().add(redisKey,"mc",90);
        redisTemplate.opsForZSet().add(redisKey,"wc",80);
        redisTemplate.opsForZSet().add(redisKey,"zhr",70);
        redisTemplate.opsForZSet().add(redisKey,"ht",60);
        System.out.println(redisTemplate.opsForZSet().zCard(redisKey));
        System.out.println(redisTemplate.opsForZSet().score(redisKey,"mc"));
        System.out.println(redisTemplate.opsForZSet().rank(redisKey,"mc"));
        System.out.println(redisTemplate.opsForZSet().reverseRank(redisKey,"mc"));
        System.out.println(redisTemplate.opsForZSet().reverseRange(redisKey,0,2));
    }
    @Test
    void testKeys(){
        String redisKey="test:user";
        System.out.println(redisTemplate.hasKey(redisKey));
        redisTemplate.delete(redisKey);
        System.out.println(redisTemplate.hasKey(redisKey));
        redisTemplate.expire("test:students",10, TimeUnit.SECONDS);

    }

    /**
     * 多次访问同一个key
     * 绑定
     */
    @Test
    void testBoundOperations(){
        String redisKey="test:count";
        BoundValueOperations operations=redisTemplate.boundValueOps(redisKey);
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        operations.increment();
        System.out.println(operations.get());
    }

    /**
     * 编程式事务
     */
    @Test
    void testTransaction1(){
        redisTemplate.delete("test:ts");
        Object obj = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                String redisKey="test:ts";
                //启用事务
                redisOperations.multi();
                redisOperations.opsForSet().add(redisKey,"李四");
                redisOperations.opsForSet().add(redisKey,"张三");
                redisOperations.opsForSet().add(redisKey,"王五");
                //事务中的查询无效
                System.out.println(redisOperations.opsForSet().members(redisKey));
                return redisOperations.exec();
            }
        });
        System.out.println(obj);
    }
}
