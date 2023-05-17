package com.loyear.springboot.service;

import com.loyear.springboot.controller.dto.UserDTO;
import com.loyear.springboot.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author loyear
 * @since 2023-01-06
 */
public interface IUserService extends IService<User> {

    UserDTO login(UserDTO userDto);

    User register(UserDTO userDto);
}
