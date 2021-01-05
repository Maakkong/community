package com.nowcoder.community.quartz;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.ElasticSearchService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.RedisKeyUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2021/1/5 16:27
 * @Description No Description
 */
public class PostScoreRefreshJob implements Job, CommunityConstant {

    private static Logger logger=LoggerFactory.getLogger(PostScoreRefreshJob.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ElasticSearchService elasticSearchService;

    private static final Date epoch;

    static {
        try {
            epoch=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2014-08-01 00:00:00");
        }catch (ParseException e){
            throw new RuntimeException("初始化牛客纪元失败！");
        }
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String redisKey= RedisKeyUtil.getPostScoreKey();
        BoundSetOperations operations=redisTemplate.boundSetOps(redisKey);
        if(operations.size()==0){
            logger.info("任务取消！没有刷新的帖子");
            return;
        }

        logger.info("任务开始！正在刷新帖子分数："+operations.size());
        while (operations.size()>0){
            this.refresh((Integer) operations.pop());
        }
        logger.info("任务结束！刷新分数完成");
    }

    private void refresh(int postId){
        DiscussPost post = discussPostService.findDiscussPostById(postId);
        if(post==null){
            logger.error("该帖子不存在：id= "+postId);
            return;
        }
        //是否加精
        boolean wonderful=post.getStatus()==1;
        //评论数量
        int commentCount=post.getCommentCount();
        //点赞数量
        long likeCount=likeService.findEntityLikeCount(ENTITY_TYPE_POST,postId);

        //计算权重
        double w=(wonderful?75:0)+commentCount*10+likeCount*2;
        //分数=权重+时间间隔
        double score=Math.log10(Math.max(w,1))+(post.getCreateTime().getTime()-epoch.getTime())/(1000*3600*24);
        //更新数据库
        discussPostService.updateScore(postId,score);
        //搜索数据更新
        post.setScore(score);
        elasticSearchService.saveDiscussPost(post);
    }

}
