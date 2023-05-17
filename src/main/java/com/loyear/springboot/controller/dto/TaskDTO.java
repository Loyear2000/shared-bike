package com.loyear.springboot.controller.dto;

import com.loyear.springboot.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 接受前端登录请求参数
 */

@Data
public class TaskDTO {
    private String username;
    private String bikeId;
    private String info;
    private String type;
    private Integer faultLevel;
    private Boolean ifScrap;
    private String repairPoint;
    private String scrapPoint;





}
