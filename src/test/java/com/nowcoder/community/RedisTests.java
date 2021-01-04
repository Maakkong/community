package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.*;
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

    /**
     * 统计20万重复数据的独立总数
     * 标准误差 0.81%
     */
    @Test
    void testHyperLoglog(){
        String redisKey="test:hyper:01";

        for (int i = 1; i <=100000 ; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey,i);
        }

        for (int i = 1; i <=100000 ; i++) {
            int r= (int) (Math.random()*100000+1);
            redisTemplate.opsForHyperLogLog().add(redisKey,r);
        }

        System.out.println(redisTemplate.opsForHyperLogLog().size(redisKey));
    }

    /**
     * 将3组数据合并，统计合并后的重复数据的独立总数
     */
    @Test
    void testHyperLoglogUnion(){
        String redisKey2="test:hyper:02";

        for (int i = 1; i <=10000 ; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey2,i);
        }

        String redisKey3="test:hyper:03";

        for (int i = 5001; i <=15000 ; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey3,i);
        }

        String redisKey4="test:hyper:04";

        for (int i = 1001; i <=20000 ; i++) {
            redisTemplate.opsForHyperLogLog().add(redisKey4,i);
        }

        String unionKey="test:hyper:union";

        redisTemplate.opsForHyperLogLog().union(unionKey,redisKey2,redisKey3,redisKey4);

        System.out.println(redisTemplate.opsForHyperLogLog().size(unionKey));
    }

    /**
     * 统计一组数据的布尔值
     */
    @Test
    public void testBitMap(){
        String redisKey="test:bm:01";
        //记录
        redisTemplate.opsForValue().setBit(redisKey,1,true);
        redisTemplate.opsForValue().setBit(redisKey,4,true);
        redisTemplate.opsForValue().setBit(redisKey,7,true);

        //查询
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,2));

        //统计,返回true的个数
        Object obj=redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(obj);
    }

    /**
     * 统计三组数据的布尔值，并对3组数据做OR运算
     */
    @Test
    void testBitMapOperation(){
        String redisKey2="test:bm:02";
        redisTemplate.opsForValue().setBit(redisKey2,0,true);
        redisTemplate.opsForValue().setBit(redisKey2,1,true);
        redisTemplate.opsForValue().setBit(redisKey2,2,true);

        String redisKey3="test:bm:03";
        redisTemplate.opsForValue().setBit(redisKey3,2,true);
        redisTemplate.opsForValue().setBit(redisKey3,3,true);
        redisTemplate.opsForValue().setBit(redisKey3,4,true);

        String redisKey4="test:bm:04";
        redisTemplate.opsForValue().setBit(redisKey4,4,true);
        redisTemplate.opsForValue().setBit(redisKey4,5,true);
        redisTemplate.opsForValue().setBit(redisKey4,6,true);

        String redisKey="test:bm:or";

        Object obj=redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(),
                        redisKey2.getBytes(),redisKey3.getBytes(),redisKey4.getBytes()
                );
                return connection.bitCount(redisKey.getBytes());
            }
        });
        System.out.println(obj);
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,0));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,1));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,2));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,3));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,4));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,5));
        System.out.println(redisTemplate.opsForValue().getBit(redisKey,6));
    }
}
