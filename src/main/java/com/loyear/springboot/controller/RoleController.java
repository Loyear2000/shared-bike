package com.loyear.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loyear.springboot.common.Result;
import com.loyear.springboot.entity.Menu;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.loyear.springboot.service.IRoleService;
import com.loyear.springboot.entity.Role;

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
@RequestMapping("/role")
public class RoleController {

    @Resource
    private IRoleService roleService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Role role){
        return Result.success(roleService.saveOrUpdate(role));
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success(roleService.removeById(id));
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(roleService.removeByIds(ids));
    }

    @GetMapping
    public Result findAll(){
        return Result.success(roleService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id){
        return Result.success(roleService.getById(id));
    }

    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam String name){
        QueryWrapper<Role> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        queryWrapper.like("name",name);
        return Result.success(roleService.page(new Page<>(pageNum,pageSize),queryWrapper));
    }


    @PostMapping("/roleMenu/{roleId}")
    public Result roleMenu(@PathVariable Integer roleId,@RequestBody List<Integer>  menuIds){
        roleService.setRoleMenu(roleId,menuIds);
        return Result.success();
    }

    @GetMapping("/roleMenu/{roleId}")
    public Result getRoleMenu(@PathVariable Integer roleId){
        return Result.success(roleService.getRoleMenu(roleId));
    }
}

