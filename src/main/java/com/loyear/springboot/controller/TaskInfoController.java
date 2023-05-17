package com.loyear.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loyear.springboot.entity.FaultSheet;
import com.loyear.springboot.entity.User;
import com.loyear.springboot.service.IFaultSheetService;
import com.loyear.springboot.service.IUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import com.loyear.springboot.common.Result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.loyear.springboot.service.ITaskInfoService;
import com.loyear.springboot.entity.TaskInfo;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loyear
 * @since 2023-03-11
 */
@RestController
@RequestMapping("/taskInfo")
public class TaskInfoController {

    @Resource
    private ITaskInfoService taskInfoService;

    @Resource
    private IFaultSheetService faultSheetService;

    @Resource
    private IUserService userService;



    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody TaskInfo taskInfo){
        return Result.success(taskInfoService.saveOrUpdate(taskInfo));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(taskInfoService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(taskInfoService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll(){
        return Result.success(taskInfoService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(taskInfoService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize){
        QueryWrapper<TaskInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return Result.success(taskInfoService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }

    @GetMapping("/look")
    public Result Look(@RequestParam String bikeId){
        QueryWrapper<TaskInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("bike_id",bikeId);
        List<TaskInfo> taskInfo = taskInfoService.list(queryWrapper);
        return Result.success(taskInfo);
    }

    @GetMapping("/data")
    public Result getData(@RequestParam String username,@RequestParam String role){
        HashMap<Object, Object> hashMap = new HashMap<>();
        QueryWrapper<TaskInfo> queryWrapper=new QueryWrapper<>();
        QueryWrapper<TaskInfo> queryWrapper1=new QueryWrapper<>();
        QueryWrapper<TaskInfo> queryWrapper2=new QueryWrapper<>();
        queryWrapper.eq("username",username);
        queryWrapper.like("create_time", LocalDate.now());
        List<TaskInfo> dayTaskList = taskInfoService.list(queryWrapper);

        queryWrapper1.eq("username",username);
        queryWrapper1.like("create_time", StringUtils.substring(LocalDate.now().toString(),0,6));
        List<TaskInfo> monthTaskList = taskInfoService.list(queryWrapper1);

        int count=0;
        for(TaskInfo item:monthTaskList){
            QueryWrapper<FaultSheet> conditon=new QueryWrapper<>();
            conditon.eq("bike_id",item.getBikeId());
            if(faultSheetService.getOne(conditon).getIfLate()==1){
                count++;
            }
        }

        double money=0;
        if("ROLE_OPER".equals(role)){
            money=monthTaskList.size()*0.3-count*0.1;
        }else if("ROLE_REPAIR".equals(role)){
            money=monthTaskList.size()*3-count*1;
        }


        hashMap.put("dayTask",dayTaskList.size());
        hashMap.put("monthTask",monthTaskList.size());
        hashMap.put("lateTask",count);
        hashMap.put("money",money);

        QueryWrapper<User> queryWrapper3 = new QueryWrapper<>();
        queryWrapper3.eq("username",username);
        User user = userService.getOne(queryWrapper3);
        user.setMoney(money);
        userService.saveOrUpdate(user);


        return Result.success(hashMap);
    }


}

