package com.nowcoder.community.service.impl;

import com.nowcoder.community.service.DataService;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2021/1/4 18:44
 * @Description No Description
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private RedisTemplate redisTemplate;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @Override
    public void recordUV(String ip) {
        String redisKey= RedisKeyUtil.getUVKey(dateFormat.format(new Date()));

        redisTemplate.opsForHyperLogLog().add(redisKey,ip);
    }

    @Override
    public long calculateUV(Date startdate, Date enddate) {
        if(startdate==null||enddate==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //整理Key
        List<String> keyList = new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(startdate);
        while (!calendar.getTime().after(enddate)){
            String key=RedisKeyUtil.getUVKey(dateFormat.format(calendar.getTime()));
            keyList.add(key);
            calendar.add(Calendar.DATE,1);
        }
        String redisKey=RedisKeyUtil.getUVKey(dateFormat.format(startdate),dateFormat.format(enddate));
        redisTemplate.opsForHyperLogLog().union(redisKey,keyList.toArray());

        return redisTemplate.opsForHyperLogLog().size(redisKey);
    }

    @Override
    public void recordDAU(Integer userId) {
        String redisKey= RedisKeyUtil.getDAUKey(dateFormat.format(new Date()));
        redisTemplate.opsForValue().setBit(redisKey,userId,true);
    }

    @Override
    public long calculateDAU(Date startdate, Date enddate) {
        if(startdate==null||enddate==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        //整理Key
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(startdate);
        while (!calendar.getTime().after(enddate)){
            String key=RedisKeyUtil.getDAUKey(dateFormat.format(calendar.getTime()));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE,1);
        }
        //进行OR运算,期间内有一次登录就算活跃
        return (long) redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                String redisKey=RedisKeyUtil.getDAUKey(dateFormat.format(startdate),dateFormat.format(enddate));
                connection.bitOp(RedisStringCommands.BitOperation.OR,
                        redisKey.getBytes(),keyList.toArray(new byte[0][0]));

                return connection.bitCount(redisKey.getBytes());
            }
        });
    }
}
