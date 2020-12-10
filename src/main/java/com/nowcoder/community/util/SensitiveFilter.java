package com.nowcoder.community.util;

import lombok.Data;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/12/2 13:51
 * @Description 敏感词过滤工具类
 */
@Component
public class SensitiveFilter {

    private static final Logger logger=LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符
    private static final String REPLACEMENT="***";
    //初始话前缀树
    private TrieNode root=new TrieNode();

    /**
     * 初始化方法，服务启动时初始化
     */
    @PostConstruct
    public void init(){
        //类路径下获取文件
        try(
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword=reader.readLine())!=null){
                //添加到前缀树
                this.addKeyWord(keyword);
            }
        }catch (IOException e){
            logger.error("加载敏感词文件失败："+e.getMessage());
        }
    }

    /**
     * 将敏感词添加到前缀树
     * @param keyword
     */
    private void addKeyWord(String keyword){
        TrieNode tempNode=root;
        for (int i=0;i<keyword.length();i++){
            char c=keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode==null){
                //初始化子节点
                subNode=new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            //指向子节点
            tempNode=subNode;
            //结束标识
            if(i==keyword.length()-1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤文本
     * @return 过滤后文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }
        //指针1，指向树
        TrieNode tempNode=root;
        //指针2
        int begin=0;
        //指针3
        int position=0;
        //结果串
        StringBuilder sb=new StringBuilder();

        //🔣赌🔣博🔣
        while (position<text.length()){
            char c=text.charAt(position);
            //跳过符号
            if(isSymbol(c)){
                //指针在根节点,将符号计入结果,指针2前进
                if(tempNode==root){
                    sb.append(c);
                    begin++;
                }
                //指针3前进，与符号无关
                position++;
                continue;
            }
            //检查下级节点
            tempNode=tempNode.getSubNode(c);
            if(tempNode==null){
                //以begin开头的不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                //指针1归位
                tempNode=root;
            }else if(tempNode.isKeyWordEnd()) {
                //发现敏感词
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin = ++position;
                tempNode=root;
            }else {
                //指针3后移
                position++;
            }
        }
        //补上最后一批字符
        sb.append(text.substring(begin));
        return sb.toString();
    }

    /**
     * 判断是否为符号
     * @param c
     * @return
     */
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    /**
     * 前缀树
     */
    @Data
    private class TrieNode {
        //关键词结束标识
        private boolean isKeyWordEnd=false;

        //子节点 (Key是下级节点字符，value是下级节点)
        private Map<Character,TrieNode> subNode=new HashMap<>();

        /**
         * 添加子节点
         * @param c
         * @param trieNode
         */
        public void addSubNode(Character c,TrieNode trieNode){
            subNode.put(c, trieNode);
        }

        /**
         * 获取子节点
         * @param c
         * @return
         */
        public TrieNode getSubNode(Character c){
            return subNode.get(c);
        }

    }



}
