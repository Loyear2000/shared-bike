package com.loyear.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loyear.springboot.entity.FaultSheet;
import com.loyear.springboot.entity.User;
import com.loyear.springboot.service.IFaultSheetService;
import com.loyear.springboot.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.loyear.springboot.common.Result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.loyear.springboot.service.IGridService;
import com.loyear.springboot.entity.Grid;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loyear
 * @since 2023-03-07
 */
@RestController
@RequestMapping("/grid")
public class GridController {

    @Resource
    private IGridService gridService;

    @Resource
    private IFaultSheetService faultSheetService;

    @Resource
    private IUserService userService;

    @GetMapping
    public Result findAll(){
        return Result.success(gridService.list());
    }

    @GetMapping("/distribute")
    public Result distribute(@RequestParam Integer gridId){
        QueryWrapper<FaultSheet> queryWrapper = new QueryWrapper<FaultSheet>().eq("grid_id", gridId);
        queryWrapper.eq("deal_person","").or().isNull("deal_person");
        for (FaultSheet faultSheet:faultSheetService.list(queryWrapper)){
            if (CollUtil.newArrayList(0,1,3).contains(faultSheet.getStatus())){
                String username="";
                int tasks=20;
                for (User user : userService.list(new QueryWrapper<User>().eq("role", "ROLE_OPER"))) {
                    int currentTasks = faultSheetService.list(new QueryWrapper<FaultSheet>().eq("deal_person", user.getUsername())).size();
                    if (currentTasks>20){
                        break;
                    }
                    if(currentTasks<tasks){
                        tasks=currentTasks;
                        username=user.getUsername();
                    }
                }
                faultSheet.setDealPerson(username);
                faultSheetService.saveOrUpdate(faultSheet);

            }else if(faultSheet.getStatus()==2){
                String username="";
                int tasks=20;
                for (User user : userService.list(new QueryWrapper<User>().eq("role", "ROLE_REPAIR"))) {
                    int currentTasks = faultSheetService.list(new QueryWrapper<FaultSheet>().eq("deal_person", user.getUsername())).size();
                    if (currentTasks>20){
                        break;
                    }
                    if(currentTasks<tasks){
                        tasks=currentTasks;
                        username=user.getUsername();
                    }
                }
                faultSheet.setDealPerson(username);
                faultSheetService.saveOrUpdate(faultSheet);
            }
        }


        return Result.success(gridService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(gridService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize){
        QueryWrapper<Grid> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");



        return Result.success(gridService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

    @GetMapping("/compute")
    public Result compute(){
        QueryWrapper<Grid> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");


        HashMap<Object, Object> hashMap = new HashMap<>();


        for(int i=1;i<=4;i++){
            QueryWrapper<FaultSheet> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("status",4);
            queryWrapper1.eq("grid_id",i);
            float finishRate=(float)faultSheetService.list(queryWrapper1).size()/faultSheetService.list().size();

            QueryWrapper<FaultSheet> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("if_late",1);
            queryWrapper2.eq("grid_id",i);
            float lateRate=(float)faultSheetService.list(queryWrapper2).size()/faultSheetService.list().size();


            DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
            String finishRateString = df.format(finishRate*100);
            String lateRateString = df.format(lateRate*100);


            QueryWrapper<Grid> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3.eq("grid_id",i);
            Grid one = gridService.getOne(queryWrapper3);

            one.setFinishRate(finishRateString+"%");
            one.setLateRate(lateRateString+"%");



            int allHandleTime=0;
            int count=0;
            for(FaultSheet faultSheet:faultSheetService.list(new QueryWrapper<FaultSheet>().eq("grid_id",i))){
                if(StrUtil.isNotBlank(faultSheet.getHandleTime())){
                    count++;
                    String[] strings = faultSheet.getHandleTime().split(":");
                    allHandleTime+=Integer.parseInt(strings[0])*60*60*1000;
                    allHandleTime+=Integer.parseInt(strings[1])*60*1000;
                    allHandleTime+=Integer.parseInt(strings[2])*1000;
                }
            }
            if(count==0){
                count=1;
            }
            int avgMills =  allHandleTime / count;

            long s = avgMills%3600000%60000/1000;
            long m = avgMills%3600000/60000;
            long h = avgMills / 3600000;

            String avgHandleTime=pad(2,h)+":"+pad(2,m)+":"+pad(2,s);
            one.setAvgDealTime(avgHandleTime);

            gridService.saveOrUpdate(one);
        }
        return Result.success();
    }

    public static String pad(int length,long num){
        return String.format("%0".concat(String.valueOf(length)).concat("d"), num);
    }

}

