package com.loyear.springboot.controller;


import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loyear.springboot.controller.dto.TaskDTO;
import com.loyear.springboot.entity.TaskInfo;
import com.loyear.springboot.entity.User;
import com.loyear.springboot.service.ITaskInfoService;
import io.swagger.models.auth.In;
import lombok.ToString;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import com.loyear.springboot.common.Result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.loyear.springboot.service.IFaultSheetService;
import com.loyear.springboot.entity.FaultSheet;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loyear
 * @since 2023-01-22
 */
@RestController
@RequestMapping("/faultSheet")
public class FaultSheetController {

    @Resource
    private IFaultSheetService faultSheetService;

    @Resource
    private ITaskInfoService taskInfoService;



    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(faultSheetService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(faultSheetService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll(){
        return Result.success(faultSheetService.list());
    }

    @GetMapping("/get")
    public Result get(@RequestParam String bikeId,
                      @RequestParam String dealPerson){
        QueryWrapper<FaultSheet> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("bike_id",bikeId);
        FaultSheet one = faultSheetService.getOne(queryWrapper);
        one.setDealPerson(dealPerson);
        faultSheetService.updateById(one);
        return Result.success();
    }

    @PostMapping("/submit")
    public Result submit(@RequestBody TaskDTO taskDTO){

        QueryWrapper<FaultSheet> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("bike_id",taskDTO.getBikeId());
        FaultSheet one = faultSheetService.getOne(queryWrapper);
        one.setDealPerson("");

        if("check".equals(taskDTO.getType())){
            Date date = new Date();
            Instant instant = date.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();

            LocalDateTime startTime = instant.atZone(zoneId).toLocalDateTime();
            one.setStartTime(startTime);
            LocalDateTime expectTime = startTime.plusHours(2);
            one.setExpectTime(expectTime);

            one.setFaultLevel(taskDTO.getFaultLevel());
            switch (taskDTO.getFaultLevel()){
                case 1:one.setStatus(4);
                    one.setEndTime(startTime);
                    one.setHandleTime("00:00:00");
                    break;
                case 2:one.setStatus(1);break;
                case 3:one.setStatus(3);break;
            }
        }else if("oper".equals(taskDTO.getType())){
            one.setStatus(2);
        }else if("scrap".equals(taskDTO.getType())){
            one.setStatus(4);

            Date date1 = new Date();
            Instant instant = date1.toInstant();
            ZoneId zoneId = ZoneId.systemDefault();

            LocalDateTime endTime = instant.atZone(zoneId).toLocalDateTime();
            one.setEndTime(endTime);
            if(endTime.isAfter(one.getExpectTime())){
                one.setIfLate(1);
            }

            LocalDateTime startTime=one.getStartTime();
            long mills = startTime.until(endTime, ChronoUnit.MILLIS);

            long s = mills%3600000%60000/1000;
            long m = mills%3600000/60000;
            long h = mills / 3600000;

            String handleTime=pad(2,h)+":"+pad(2,m)+":"+pad(2,s);
            one.setHandleTime(handleTime);

        }else if("repair".equals(taskDTO.getType())){
            if(taskDTO.getIfScrap()){
                one.setStatus(3);
            }else{
                Date date2 = new Date();
                Instant instant = date2.toInstant();
                ZoneId zoneId = ZoneId.systemDefault();

                LocalDateTime endTime = instant.atZone(zoneId).toLocalDateTime();
                one.setEndTime(endTime);
                if(endTime.isAfter(one.getExpectTime())){
                    one.setIfLate(1);
                }
                one.setStatus(4);

                LocalDateTime startTime=one.getStartTime();
                long mills = startTime.until(endTime, ChronoUnit.MILLIS);

                long s = mills%3600000%60000/1000;
                long m = mills%3600000/60000;
                long h = mills / 3600000;

                String handleTime=pad(2,h)+":"+pad(2,m)+":"+pad(2,s);
                one.setHandleTime(handleTime);
            }
        }

        faultSheetService.updateById(one);

        TaskInfo taskInfo = new TaskInfo();
        if("oper".equals(taskDTO.getType())){
            taskInfo.setInfo("{"+taskDTO.getRepairPoint()+"}"+taskDTO.getInfo());
        }else if("repair".equals(taskDTO.getType())){
            taskInfo.setInfo("{"+taskDTO.getScrapPoint()+"}"+taskDTO.getInfo());
        }else{
            taskInfo.setInfo(taskDTO.getInfo());
        }

        taskInfo.setBikeId(taskDTO.getBikeId());
        taskInfo.setUsername(taskDTO.getUsername());
        taskInfoService.saveOrUpdate(taskInfo);

        return Result.success();
    }

    public static String pad(int length,long num){
        return String.format("%0".concat(String.valueOf(length)).concat("d"), num);
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String status,
                           @RequestParam String bikeId,
                           @RequestParam String dealPerson){


        QueryWrapper<FaultSheet> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if(!"".equals(bikeId)){
            queryWrapper.like("bike_id",bikeId);
        }
        if(!"".equals(status)){
            queryWrapper.eq("status",Integer.valueOf(status));
        }
        if(!"".equals(dealPerson)){
            queryWrapper.like("deal_person",dealPerson);
        }

        return Result.success(faultSheetService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }




    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 方式1：(推荐) 通过 javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟javabean的属性要对应起来,且实体类需提供同toString方法
        List<FaultSheet> list = reader.readAll(FaultSheet.class);
        return Result.success(faultSheetService.saveBatch(list));


//        // 方式2：忽略表头的中文，直接读取表的内容
//        List<List<Object>> list = reader.read(1);
//        List<User> users = CollUtil.newArrayList();
//        for (List<Object> row : list) {
//            User user = new User();
//            user.setUsername(row.get(0).toString());
//            user.setPassword(row.get(1).toString());
//            user.setNickname(row.get(2).toString());
//            user.setEmail(row.get(3).toString());
//            user.setPhone(row.get(4).toString());
//            user.setAddress(row.get(5).toString());
//            user.setAvatarUrl(row.get(6).toString());
//            users.add(user);
//        }
//
//        userService.saveBatch(users);



    }






    /**
     * 导出接口
     */
    @GetMapping("/export")
    public Result export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<FaultSheet> list = faultSheetService.list();
        // 通过工具类创建writer 写出到磁盘路径
//        ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath + "/用户信息.xlsx");
        // 在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
//        writer.addHeaderAlias("username", "用户名");
//        writer.addHeaderAlias("password", "密码");
//        writer.addHeaderAlias("nickname", "昵称");
//        writer.addHeaderAlias("email", "邮箱");
//        writer.addHeaderAlias("phone", "电话");
//        writer.addHeaderAlias("address", "地址");
//        writer.addHeaderAlias("avatarUrl", "头像");
//        writer.addHeaderAlias("createTime", "创建时间");

        // 一次性写出list内的对象到excel，使用默认样式，强制输出标题
        writer.write(list, true);

        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("故障单", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
        return Result.success(true);

    }

}

