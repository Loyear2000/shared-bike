package com.loyear.springboot.service.impl;

import com.loyear.springboot.entity.Grid;
import com.loyear.springboot.mapper.GridMapper;
import com.loyear.springboot.service.IGridService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loyear
 * @since 2023-03-07
 */
@Service
public class GridServiceImpl extends ServiceImpl<GridMapper, Grid> implements IGridService {

}
