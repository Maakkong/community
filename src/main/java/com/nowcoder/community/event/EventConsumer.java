package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.ElasticSearchService;
import com.nowcoder.community.service.MessageService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/30 13:04
 * @Description No Description
 */
@Component
public class EventConsumer implements CommunityConstant {

    private static final Logger logger= LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Value("${wk.image.command}")
    private String wkImageCommand;

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @Value("${qiniu.key.access}")
    private String accessKey;

    @Value("${qiniu.key.secret}")
    private String secretKey;

    @Value("${qiniu.bucket.name}")
    private String imagesBucketName;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;


    /**
     * 消费评论等事件
     * @param record
     */
    @KafkaListener(topics = {TOPIC_COMMENT,TOPIC_LIKE,TOPIC_FOLLOW})
    public void handleCommentMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            logger.error("消息内容为空");
            return;
        }
        Event event= JSONObject.parseObject(record.value().toString(),Event.class);
        if(event==null){
            return;
        }
        //消息构造
        Message message=new Message();
        message.setFromId(SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setCreateTime(new Date());
        Map<String,Object> content=new HashMap<>();
        content.put("userId",event.getUserId());
        content.put("entityType",event.getEntityType());
        content.put("entityId",event.getEntityId());
        if(!event.getData().isEmpty()){
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(),entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);

    }

    /**
     * 消费发帖事件
     * @param record
     */
    @KafkaListener(topics = {TOPIC_PUBLISH})
    public void handlePublishMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            logger.error("消息内容为空");
            return;
        }
        Event event= JSONObject.parseObject(record.value().toString(),Event.class);
        if(event==null){
            return;
        }

        DiscussPost post = discussPostService.findDiscussPostById(event.getEntityId());
        elasticSearchService.saveDiscussPost(post);
    }

    /**
     * 消费删除事件
     * @param record
     */
    @KafkaListener(topics = {TOPIC_DELETE})
    public void handleDeleteMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            logger.error("消息内容为空");
            return;
        }
        Event event= JSONObject.parseObject(record.value().toString(),Event.class);
        if(event==null){
            return;
        }

        //在es删除帖子
        elasticSearchService.deleteDiscussPost(event.getEntityId());
    }

    /**
     * 消费分享事件
     * @param record
     */
    @KafkaListener(topics = {TOPIC_SHARE})
    public void handleShareMessage(ConsumerRecord record){
        if(record==null||record.value()==null){
            logger.error("消息内容为空");
            return;
        }
        Event event= JSONObject.parseObject(record.value().toString(),Event.class);
        if(event==null){
            return;
        }

        String htmlUrl= (String) event.getData().get("htmlUrl");
        String filename= (String) event.getData().get("fileName");
        String suffix= (String) event.getData().get("suffix");

        String cmd=wkImageCommand+" --quality 75 "+htmlUrl+" "+wkImageStorage+"/"+filename+suffix;
        try {
            //耗时长
            Runtime.getRuntime().exec(cmd);
            logger.info("生成图片成功: "+cmd);
        } catch (IOException e) {
            logger.error("s生成长图失败 "+e.getMessage());
        }
        //启用定时器监视该图片，图片生成则上传七牛云

        UploadTask uploadTask=new UploadTask(filename,suffix);
        Future future = taskScheduler.scheduleAtFixedRate(uploadTask, 500);
        uploadTask.setFuture(future);
    }

    class UploadTask implements Runnable{

        //文件名
        private String filename;
        //文件后缀
        private String suffix;
        //启动任务的返回值
        private Future future;
        //开始事件
        private long startTime;
        //上传次数
        private int uploadTimes;

        public UploadTask(String filename, String suffix) {
            this.filename = filename;
            this.suffix = suffix;
            this.startTime=System.currentTimeMillis();
            this.uploadTimes=0;
        }

        public void setFuture(Future future) {
            this.future = future;
        }

        @Override
        public void run() {
            //生成图片失败
            if(System.currentTimeMillis()-startTime>30000){
                logger.error("执行时间过长，终止任务！"+filename);
                future.cancel(true);
                return;
            }
            //上传失败
            if(uploadTimes>=3){
                logger.error("上传次数过多，终止任务！"+filename);
                future.cancel(true);
                return;
            }

            String path=wkImageStorage+"/"+filename+suffix;
            File file=new File(path);
            if(file.exists()){
                logger.info(String.format("开始第%d次上传[%s]",++uploadTimes,filename));
                //设置响应信息
                StringMap policy=new StringMap();
                policy.put("returnBody", CommunityUtil.getJSONString(0));
                //上传凭证
                Auth auth=Auth.create(accessKey,secretKey );
                String uploadToken=auth.uploadToken(imagesBucketName,filename,3600,policy);
                //指定上传机房
                UploadManager manager=new UploadManager(new Configuration(Zone.autoZone()));
                try{
                    //上传图片
                    Response response=manager.put(path,filename,uploadToken,null,"image/"+suffix,false);
                    //处理响应结果
                    JSONObject json = JSONObject.parseObject(response.bodyString());
                    if(json==null||json.get("code")==null||!json.get("code").toString().equals("0")){
                        logger.error(String.format("第%d次上传[%s]失败",uploadTimes,filename));
                    }else {
                        logger.info(String.format("第%d次上传[%s]成功",uploadTimes,filename));
                        future.cancel(true);
                    }
                }catch (QiniuException e){
                    logger.info(String.format("第%d次上传[%s]失败",uploadTimes,filename));
                }
            }
            else {
                logger.info("等待图片文件生成:["+filename+"].");
            }
        }
    }
}
