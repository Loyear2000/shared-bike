package com.loyear.springboot.mapper;

import com.loyear.springboot.entity.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author loyear
 * @since 2023-01-08
 */
public interface RoleMapper extends BaseMapper<Role> {

    @Select("select id from sys_role where flag=#{flag}")
    Integer selectByFlag(String flag);
}
