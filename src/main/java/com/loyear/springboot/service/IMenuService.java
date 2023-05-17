package com.loyear.springboot.service;

import com.loyear.springboot.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author loyear
 * @since 2023-01-08
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
