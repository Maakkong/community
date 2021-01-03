package com.nowcoder.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import com.nowcoder.community.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/20 15:04
 * @Description No Description
 */
@Controller
public class MessageController implements CommunityConstant {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 私信列表
     * @param model
     * @param page
     * @return
     */
    @RequestMapping(path = "/letter/list",method = RequestMethod.GET)
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConvesationCount(user.getId()));
        //会话列表
        List<Message> conversationList = messageService
                .findConversations(user.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> conversations=new ArrayList<>();
        if(conversationList!=null){
            for (Message message : conversationList) {
                Map<String, Object> map=new HashMap<>();
                map.put("conversation",message);
                map.put("letterCount",messageService.findLetterCount(message.getConversationId()));
                map.put("unreadCount",messageService.findLetterUnreadCount(user.getId(),message.getConversationId()));
                //当前用户是发起方还是接受方
                int targetId=user.getId().equals(message.getFromId())?message.getToId():message.getFromId();
                map.put("target",userService.findUserById(targetId));

                conversations.add(map);
            }
        }
        model.addAttribute("conversations",conversations);
        //查询用户所有的未读消息数量
        int letterUnreadCount=messageService.findLetterUnreadCount(user.getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        //未读通知数量
        int noticeUnreadCount=messageService.findNoticeUnreadCount(user.getId(),null);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);
        return "/site/letter";
    }

    /**
     * 私信会话详情
     * @param conversationId
     * @param model
     * @param page
     * @return
     */
    @RequestMapping(path = "/letter/detail/{conversationId}",method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId,Model model,Page page){
        page.setLimit(5);
        page.setPath("/letter/detail/"+conversationId);
        page.setRows(messageService.findLetterCount(conversationId));
        //私信列表
        List<Message> letterList = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> letters=new ArrayList<>();
        if(letterList!=null){
            for (Message message : letterList) {
                Map<String, Object> map=new HashMap<>();
                map.put("letter",message);
                map.put("fromUser",userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters",letters);
        //私信目标
        model.addAttribute("target",getLetterTarget(conversationId));
        List<Integer> lettersIds = getLettersIds(letterList);
        if(lettersIds!=null&&!lettersIds.isEmpty()){
            messageService.readMessage(lettersIds);
        }
        return "site/letter-detail";
    }

    /**
     * 获取私信目标
     * @param conversationId
     * @return
     */
    private User getLetterTarget(String conversationId){
        String ids[]=conversationId.split("_");
        int id0=Integer.parseInt(ids[0]);
        int id1=Integer.parseInt(ids[1]);
        if(hostHolder.getUser().getId().equals(id0)){
            return userService.findUserById(id1);
        }
        return userService.findUserById(id0);
    }

    /**
     * 获取未读消息id
     * @param letterList
     * @return
     */
    private List<Integer> getLettersIds(List<Message> letterList){
        List<Integer> ids = new ArrayList<>();
        if(letterList!=null){
            User user = hostHolder.getUser();
            for (Message message : letterList) {
                if(message.getToId().equals(user.getId()) && message.getStatus()==0){
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    /**
     * 发私信
     * @param toName
     * @param content
     * @return
     */
    @RequestMapping(path = "/letter/send",method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName,String content){
        User userByName = userService.findUserByName(toName);
        if(userByName==null){
            return CommunityUtil.getJSONString(1,"目标用户不存在");
        }
        Message message=new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(userByName.getId());
        if(message.getFromId()<message.getToId()){
            message.setConversationId(message.getFromId()+"_"+message.getToId());
        } else {
            message.setConversationId(message.getToId()+"_"+message.getFromId());
        }
        message.setContent(content);
        message.setStatus(0);
        message.setCreateTime(new Date());
        messageService.addMessage(message);
        return CommunityUtil.getJSONString(0,"发送成功");
    }

    /**
     * 通知列表
     * @param model
     * @return
     */
    @RequestMapping(value = "/notice/list",method = RequestMethod.GET)
    public String getNoticeList(Model model){
        User user = hostHolder.getUser();

        //查询评论类
        Message message = messageService.findLatestNotice(user.getId(), TOPIC_COMMENT);
        if(message!=null){
            Map<String, Object> messageVo = new HashMap();
            messageVo.put("message",message);

            String content= HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVo.put("user",userService.findUserById((Integer) data.get("userId")));
            messageVo.put("entityType",data.get("entityType"));
            messageVo.put("entityId",data.get("entityId"));
            messageVo.put("postId",data.get("postId"));

            int count=messageService.findNoticeCount(user.getId(),TOPIC_COMMENT);
            messageVo.put("count",count);
            int unreadCount=messageService.findNoticeUnreadCount(user.getId(),TOPIC_COMMENT);
            messageVo.put("unreadCount",unreadCount);
            model.addAttribute("commentNotice",messageVo);
        }


        //点赞通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_LIKE);
        if(message!=null){
            Map<String, Object> messageVo = new HashMap();
            messageVo = new HashMap();
            messageVo.put("message",message);

            String content= HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVo.put("user",userService.findUserById((Integer) data.get("userId")));
            messageVo.put("entityType",data.get("entityType"));
            messageVo.put("entityId",data.get("entityId"));
            messageVo.put("postId",data.get("postId"));

            int count=messageService.findNoticeCount(user.getId(),TOPIC_LIKE);
            messageVo.put("count",count);
            int unreadCount=messageService.findNoticeUnreadCount(user.getId(),TOPIC_LIKE);
            messageVo.put("unreadCount",unreadCount);
            model.addAttribute("likeNotice",messageVo);
        }


        //关注通知
        message = messageService.findLatestNotice(user.getId(), TOPIC_FOLLOW);
        if(message!=null){
            Map<String, Object> messageVo = new HashMap();
            messageVo.put("message",message);

            String content= HtmlUtils.htmlUnescape(message.getContent());
            Map<String, Object> data = JSONObject.parseObject(content, HashMap.class);

            messageVo.put("user",userService.findUserById((Integer) data.get("userId")));
            messageVo.put("entityType",data.get("entityType"));
            messageVo.put("entityId",data.get("entityId"));

            int count=messageService.findNoticeCount(user.getId(),TOPIC_FOLLOW);
            messageVo.put("count",count);
            int unreadCount=messageService.findNoticeUnreadCount(user.getId(),TOPIC_FOLLOW);
            messageVo.put("unreadCount",unreadCount);
            model.addAttribute("followNotice",messageVo);
        }
        //查询未读消息数量
        //私信
        int letterUnreadCount=messageService.findLetterUnreadCount(user.getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);
        //通知
        int noticeUnreadCount=messageService.findNoticeUnreadCount(user.getId(),null);
        model.addAttribute("noticeUnreadCount",noticeUnreadCount);

        return "site/notice";
    }


    @RequestMapping(value = "/notice/detail/{topic}",method = RequestMethod.GET)
    public String getNoticeDetail(@PathVariable("topic") String topic,Page page,Model model){
        User user = hostHolder.getUser();
        page.setLimit(5);
        page.setPath("/notice/detail/"+topic);
        page.setRows(messageService.findNoticeCount(user.getId(),topic));

        List<Message> noticeList = messageService.findNotices(user.getId(), topic, page.getOffset(), page.getLimit());
        List<Map<String, Object>> noticeVoList = new ArrayList<>();
        if(noticeList!=null){
            for (Message notice : noticeList) {
                Map<String, Object> map=new HashMap<>();
                //通知
                map.put("notice",notice);
                //内容
                String content=HtmlUtils.htmlUnescape(notice.getContent());
                Map<String, Object> data=JSONObject.parseObject(content,HashMap.class);
                //发消息的用户
                map.put("user",userService.findUserById((Integer) data.get("userId")));
                map.put("entityType",data.get("entityType"));
                map.put("entityId",data.get("entityId"));
                map.put("postId",data.get("postId"));
                //通知的作者
                map.put("fromUser",userService.findUserById(notice.getFromId()));
                noticeVoList.add(map);
            }
        }
        model.addAttribute("notices",noticeVoList);
        //设置已读
        List<Integer> ids=getLettersIds(noticeList);
        if(ids!=null && !ids.isEmpty()){
            messageService.readMessage(ids);
        }

        return "site/notice-detail";
    }

}
