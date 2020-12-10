package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/3 14:12
 * @Description 帖子控制器
 */
@Controller
@RequestMapping("/discuss")
public class DicussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 添加帖子
     * @param title
     * @param content
     * @return
     */
    @RequestMapping(path = "/add",method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content){
        User user = hostHolder.getUser();
        if(user==null){
            return CommunityUtil.getJSONString(403, "没有登录!");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        Map<String, Object> map=new HashMap<>();
        map.put("row",discussPostService.addDiscussPost(post));
        // 报错的情况,将来统一处理.
        map.get("row");
        return CommunityUtil.getJSONString(0, "发布成功!",map);
    }

    @RequestMapping(path = "/detail/{id}",method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("id") Integer id, Model model){
        DiscussPost discussPostById = discussPostService.findDiscussPostById(id);
        model.addAttribute("post",discussPostById);
        User userById = userService.findUserById(discussPostById.getUserId());
        model.addAttribute("user",userById);

        return "site/discuss-detail";
    }

}
