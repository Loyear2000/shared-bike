package com.loyear.springboot.service.impl;

import com.loyear.springboot.entity.CheckLog;
import com.loyear.springboot.mapper.CheckLogMapper;
import com.loyear.springboot.service.ICheckLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loyear
 * @since 2023-03-13
 */
@Service
public class CheckLogServiceImpl extends ServiceImpl<CheckLogMapper, CheckLog> implements ICheckLogService {

}
