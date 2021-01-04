package com.nowcoder.community.controller;

import com.nowcoder.community.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * @author 不白而痴
 * @version 1.0
 * @date 2021/1/4 19:11
 * @Description No Description
 */
@Controller
public class DataController {

    @Autowired
    private DataService dataService;

    /**
     * data页面
     * @return
     */
    @RequestMapping(value = "/data",method = {RequestMethod.GET,RequestMethod.POST})
    public String getDataPage(){
        return "/site/admin/data";
    }

    @RequestMapping(path = "/data/uv",method = RequestMethod.POST)
    public String getUV(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                        Model model){
        long uv=dataService.calculateUV(startDate,endDate);
        model.addAttribute("uvResult",uv);
        model.addAttribute("uvStartDate",startDate);
        model.addAttribute("uvEndDate",endDate);
        //可以直接使用模板也可以 forward转发，转发在一个请求，所以/data需要支持post请求
        //forward 请求处理一半，其余靠另一个方法/data处理,
        return "forward:/data";
    }

    @RequestMapping(path = "/data/dau",method = RequestMethod.POST)
    public String getDAU(@DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                        Model model){
        long dau=dataService.calculateDAU(startDate,endDate);
        model.addAttribute("dauResult",dau);
        model.addAttribute("dauStartDate",startDate);
        model.addAttribute("dauEndDate",endDate);
        return "forward:/data";
    }


}
