package com.nowcoder.community.service;

import java.util.Date;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2021/1/4 18:43
 * @Description No Description
 */
public interface DataService {

    /**
     * 将指定ip存入UV
     * @param ip
     */
    void recordUV(String ip);

    /**
     * 统计指定日期内的UV
     * @param startdate
     * @param enddate
     * @return
     */
    long calculateUV(Date startdate, Date enddate);

    /**
     * 将指定用户计入DAU
     * @param userId
     */
    void recordDAU(Integer userId);

    /**
     * 统计指定日期内的DAU
     * @param startdate
     * @param enddate
     * @return
     */
    long calculateDAU(Date startdate,Date enddate);
}
