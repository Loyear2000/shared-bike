package com.loyear.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("sys_param")
@Data
public class Param {
    private String name;
    private String value;
    private String type;

}
