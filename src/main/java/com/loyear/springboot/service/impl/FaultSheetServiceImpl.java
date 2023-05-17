package com.loyear.springboot.service.impl;

import com.loyear.springboot.entity.FaultSheet;
import com.loyear.springboot.mapper.FaultSheetMapper;
import com.loyear.springboot.service.IFaultSheetService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author loyear
 * @since 2023-01-22
 */
@Service
public class FaultSheetServiceImpl extends ServiceImpl<FaultSheetMapper, FaultSheet> implements IFaultSheetService {

}
