package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/22 17:26
 * @Description No Description
 */
@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(Integer entityType,Integer entityId,Integer entityUserId){
        User user = hostHolder.getUser();
        if(user==null){
            return CommunityUtil.getJSONString(1,"请先登录");
        }
        //点赞
        likeService.like(user.getId(),entityType,entityId,entityUserId);
        //数量
        long likeCount=likeService.findEntityLikeCount(entityType,entityId);
        //状态
        long likeStatus=likeService.findEntityLikeStatus(user.getId(),entityType,entityId);
        Map<String,Object> map=new HashMap();
        map.put("likeCount",likeCount);
        map.put("likeStatus",likeStatus);
        return CommunityUtil.getJSONString(0,null,map);
    }
}
