package com.loyear.springboot.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loyear.springboot.common.Result;
import com.loyear.springboot.entity.FaultSheet;
import com.loyear.springboot.entity.User;
import com.loyear.springboot.service.IFaultSheetService;
import com.loyear.springboot.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private IUserService userService;

    @Resource
    private IFaultSheetService faultSheetService;


    @GetMapping("/fault")
    public Result fault() {
        LocalDate now = LocalDate.now();
        ArrayList<Integer> numberList = new ArrayList<>();

        QueryWrapper<FaultSheet> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.like("create_time",now);
        QueryWrapper<FaultSheet> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.like("create_time",now.minusDays(1));
        QueryWrapper<FaultSheet> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.like("create_time",now.minusDays(2));
        QueryWrapper<FaultSheet> queryWrapper4 = new QueryWrapper<>();
        queryWrapper4.like("create_time",now.minusDays(3));
        QueryWrapper<FaultSheet> queryWrapper5 = new QueryWrapper<>();
        queryWrapper5.like("create_time",now.minusDays(4));
        numberList.add(faultSheetService.list(queryWrapper1).size());
        numberList.add(faultSheetService.list(queryWrapper2).size());
        numberList.add(faultSheetService.list(queryWrapper3).size());
        numberList.add(faultSheetService.list(queryWrapper4).size());
        numberList.add(faultSheetService.list(queryWrapper5).size());

        ArrayList<String> dateList = new ArrayList<>();
        dateList.add(now.toString());
        dateList.add(now.minusDays(1).toString());
        dateList.add(now.minusDays(2).toString());
        dateList.add(now.minusDays(3).toString());
        dateList.add(now.minusDays(4).toString());
        Collections.reverse(dateList);

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("dateList",dateList);
        hashMap.put("numberList",numberList);


        return Result.success(hashMap);
    }

    @GetMapping("/level")
    public Result level() {
        ArrayList<Integer> numberList = new ArrayList<>();

        for(int i=1;i<=3;i++){
            QueryWrapper<FaultSheet> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("fault_level",i);
            queryWrapper.like("create_time",LocalDate.now());
            numberList.add(faultSheetService.list(queryWrapper).size());
        }


        return Result.success(numberList);
    }

    @GetMapping("/status")
    public Result status() {
        ArrayList<Integer> numberList = new ArrayList<>();

        for(int i=0;i<=4;i++){
            QueryWrapper<FaultSheet> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status",i);
            queryWrapper.like("create_time",LocalDate.now());
            numberList.add(faultSheetService.list(queryWrapper).size());
        }


        return Result.success(numberList);
    }


    @GetMapping("/parts")
    public Result parts() {
        ArrayList<Integer> numberList = new ArrayList<>();

        for(int i=1;i<13;i++){
            QueryWrapper<FaultSheet> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("fault_parts",i);
            queryWrapper.like("create_time",LocalDate.now());
            numberList.add(faultSheetService.list(queryWrapper).size());
        }


        return Result.success(numberList);
    }
}
