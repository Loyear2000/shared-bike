package com.loyear.springboot.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loyear.springboot.common.Constants;
import com.loyear.springboot.controller.dto.UserDTO;
import com.loyear.springboot.entity.Menu;
import com.loyear.springboot.entity.User;
import com.loyear.springboot.exception.ServiceException;
import com.loyear.springboot.mapper.RoleMapper;
import com.loyear.springboot.mapper.RoleMenuMapper;
import com.loyear.springboot.mapper.UserMapper;
import com.loyear.springboot.service.IMenuService;
import com.loyear.springboot.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loyear.springboot.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loyear
 * @since 2023-01-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG=Log.get();

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;


    @Override
    public UserDTO login(UserDTO userDto) {
        User one = getUserInfo(userDto);
        if(one!=null){
            BeanUtil.copyProperties(one,userDto,true);//赋值（目标对象与源对象的相同属性）
//                System.out.println(userDto);//打印看下当前userDto
            String token = TokenUtils.genToken(one.getId().toString(), one.getPassword());
            userDto.setToken(token);

            String role = one.getRole();
            // 设置用户的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            userDto.setMenus(roleMenus);

            return userDto;
        }else{
            throw new ServiceException(Constants.CODE_600,"用户名或密码错误");
        }
    }

    @Override
    public User register(UserDTO userDto) {
        User one = getUserInfo(userDto);
        if (one == null) {
            one = new User();
            BeanUtil.copyProperties(userDto, one, true);
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    private User getUserInfo(UserDTO userDto){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDto.getUsername());
        queryWrapper.eq("password", userDto.getPassword());
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
//            e.printStackTrace();
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500,"系统异常");
        }
        return one;

    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
