package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/10 15:12
 * @Description No Description
 */
@Mapper
public interface CommentMapper {

    /**
     * 根据实体查询
     * @return
     */
    List<Comment> selectCommentByEntity(@Param("entityType") Integer entityType,
                                        @Param("entityId") Integer entityId,
                                        @Param("offset") Integer offset,
                                        @Param("limit") Integer limit);

    /**
     * 根据实体查询总数
     * @param entityType
     * @param entityId
     * @return
     */
    int selectCountByEntity(@Param("entityType") Integer entityType,
                            @Param("entityId") Integer entityId);

}
