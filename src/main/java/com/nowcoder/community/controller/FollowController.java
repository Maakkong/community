package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/27 14:06
 * @Description No Description
 */
@Controller
public class FollowController {
    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @LoginRequired
    @RequestMapping(value = "/follow",method = RequestMethod.POST)
    @ResponseBody
    public String follow(Integer entityType,Integer entityId){
        User user = hostHolder.getUser();
        followService.follow(user.getId(),entityType,entityId);
        return CommunityUtil.getJSONString(0,"已关注！");
    }

    @LoginRequired
    @RequestMapping(value = "/unfollow",method = RequestMethod.POST)
    @ResponseBody
    public String unfollow(Integer entityType,Integer entityId){
        User user = hostHolder.getUser();
        followService.unfollow(user.getId(),entityType,entityId);
        return CommunityUtil.getJSONString(0,"已取消关注！");
    }

}
