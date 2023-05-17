package com.loyear.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loyear.springboot.common.Constants;
import com.loyear.springboot.entity.Param;
import com.loyear.springboot.mapper.ParamMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import com.loyear.springboot.common.Result;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.loyear.springboot.service.IMenuService;
import com.loyear.springboot.entity.Menu;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author loyear
 * @since 2023-01-08
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private IMenuService menuService;

    @Resource
    private ParamMapper paramMapper;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Menu menu){
        return Result.success(menuService.saveOrUpdate(menu));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(menuService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(menuService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll(@RequestParam(defaultValue = "") String name){


        return Result.success(menuService.findMenus(name));
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(menuService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam(defaultValue = "0") Integer pageNum,
                           @RequestParam(defaultValue = "99999") Integer pageSize,
                           @RequestParam String name){
        QueryWrapper<Menu> queryWrapper=new QueryWrapper<>();
        queryWrapper.like("name",name);
        return Result.success(menuService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }


    @GetMapping("/icons")
    public Result getIcons(){

        QueryWrapper<Param> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", Constants.PARAM_TYPE_ICON);
        return Result.success(paramMapper.selectList(queryWrapper));
    }

}

