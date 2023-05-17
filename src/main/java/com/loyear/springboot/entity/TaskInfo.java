package com.loyear.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author loyear
 * @since 2023-03-11
 */
@Getter
@Setter
  @TableName("task_info")
@ApiModel(value = "TaskInfo对象", description = "")
public class TaskInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("单车编号")
      private String bikeId;

      @ApiModelProperty("用户名")
      private String username;

      @ApiModelProperty("任务详情")
      private String info;



      @ApiModelProperty("任务完成时间")
      private LocalDateTime createTime;


}
