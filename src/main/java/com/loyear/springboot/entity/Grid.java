package com.loyear.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2023-03-07
 */
@Getter
@Setter
  @TableName("sys_grid")
@ApiModel(value = "Grid对象", description = "")
public class Grid implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("网格编号")
      private Integer gridId;

      @ApiModelProperty("网格名称")
      private String gridName;

      @ApiModelProperty("故障单完单率")
      private String finishRate;

      @ApiModelProperty("故障单逾期率")
      private String lateRate;

      @ApiModelProperty("故障单平均处理时长")
      private String avgDealTime;


}
