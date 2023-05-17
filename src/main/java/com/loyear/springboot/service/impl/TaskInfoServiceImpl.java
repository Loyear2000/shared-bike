package com.loyear.springboot.service.impl;

import com.loyear.springboot.entity.TaskInfo;
import com.loyear.springboot.mapper.TaskInfoMapper;
import com.loyear.springboot.service.ITaskInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loyear
 * @since 2023-03-11
 */
@Service
public class TaskInfoServiceImpl extends ServiceImpl<TaskInfoMapper, TaskInfo> implements ITaskInfoService {

}
