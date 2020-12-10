package com.nowcoder.community.dao;

import com.nowcoder.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/16 19:25
 */
@Mapper
public interface UserMapper {
    User selectById(Integer id);

    User selectByName(String username);

    User selectByEmail(String email);

    int insertUser(User user);

    int updateStatus(Integer id,int status);

    int updateHeader(Integer id,String headerUrl);

    int updatePassword(Integer id,String password);
}
