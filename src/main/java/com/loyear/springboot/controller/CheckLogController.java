package com.loyear.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import com.loyear.springboot.common.Result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.loyear.springboot.service.ICheckLogService;
import com.loyear.springboot.entity.CheckLog;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loyear
 * @since 2023-03-13
 */
@RestController
@RequestMapping("/checkLog")
public class CheckLogController {

    @Resource
    private ICheckLogService checkLogService;

    @GetMapping
    public Result findByUsername(@RequestParam String username){
        QueryWrapper<CheckLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        
        return Result.success(checkLogService.list(queryWrapper));
    }


}

