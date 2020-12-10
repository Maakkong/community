package com.nowcoder.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/16 18:32
 */
@Repository
public class AlphaDaoHibernateImpl implements AlphaDao {
    @Override
    public String select() {
        return "hibernate";
    }
}
