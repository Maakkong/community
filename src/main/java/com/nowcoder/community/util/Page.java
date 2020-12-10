package com.nowcoder.community.util;

import lombok.Data;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2020/11/17 17:08
 */
@Data
public class Page {
    /**
     * 当前页
     */
    private Integer current=1;
    /**
     * 显示上限
     */
    private Integer limit=10;
    /**
     * 数据总数
     */
    private Integer rows;
    /**
     * 查询路径（复用分页链接）
     */
    private String path;

    public void setLimit(Integer limit) {
        if(limit>=1&&limit<=100){
            this.limit = limit;
        }
    }

    public void setRows(Integer rows) {
        if(rows>0){
            this.rows = rows;
        }
    }

    /**
     * 获取当前页的起始行
     * @return
     */
    public int offsetget(){
        return (current-1)*limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal(){
        if(rows%limit==0){
            return rows/limit;
        }else{
            return rows/limit+1;
        }
    }

    /**
     *获取起始页码
     * @return
     */
    public int getFrom(){
        int from=current-2;
        return from<1?1:from;
    }

    /**
     * 获取结束页码
     * @return
     */
    public int getTo(){
        int to=current+2;
        int total=getTotal();
        return to>total?total:to;
    }
}
