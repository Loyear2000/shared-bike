package com.loyear.springboot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loyear.springboot.controller.dto.TaskDTO;
import com.loyear.springboot.entity.FaultSheet;
import com.loyear.springboot.service.IFaultSheetService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@SpringBootTest
class SpringbootApplicationTests {

    @Resource
    private IFaultSheetService faultSheetService;



    @Test
    void test(){
        LocalDate date= LocalDate.now();
        date.plusDays(1);
        System.out.println(date);




    }


    @Test
    void contextLoads() {
//        java.util.Date day=new Date();
//        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String currentTime = sdf.format(day);
//
//        System.out.println(sdf.format(day));


        Date date = new Date();
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();

        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        System.out.println("Date = " + date);
        System.out.println("LocalDateTime = " + localDateTime);
    }

    @Test
    void test1(){
        QueryWrapper<FaultSheet> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("bike_id","BIKE_001");
        FaultSheet one = faultSheetService.getOne(queryWrapper);
        LocalDateTime createTime = one.getCreateTime();
        LocalDateTime localDateTime = createTime.plusHours(2);
        System.out.println(createTime);
        System.out.println(localDateTime);

        localDateTime= LocalDateTime.parse("2023-01-22T14:12:18");

        long hour = createTime.until(localDateTime, ChronoUnit.HOURS);
        long min = createTime.until(localDateTime, ChronoUnit.MINUTES);
        long mills = createTime.until(localDateTime, ChronoUnit.MILLIS);
        System.out.println(hour);
        System.out.println(min);
        System.out.println(mills);

        Date date =new Date(mills);
        System.out.println(date);

        long s = mills%3600000%60000/1000;
        long m = mills%3600000/60000;
        long h = mills / 3600000;


        String time=pad(2,h)+":"+pad(2,m)+":"+pad(2,s);
        System.out.println(time);

    }


    /**
     * 自动补齐位数
     * @param length 补齐后的位数
     * @param num 待补齐的数值
     * @return
     */
    public static String pad(int length,long num){
        return String.format("%0".concat(String.valueOf(length)).concat("d"), num);
    }

}
