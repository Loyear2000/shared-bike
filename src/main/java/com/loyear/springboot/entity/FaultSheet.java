package com.loyear.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * 
 * </p>
 *
 * @author loyear
 * @since 2023-01-22
 */
@Getter
@Setter
@ToString
  @TableName("fault_sheet")
@ApiModel(value = "FaultSheet对象", description = "")
public class FaultSheet implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("id")
        @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("自行车id")
      private String bikeId;

      @ApiModelProperty("故障等级")
      private Integer faultLevel;

      @ApiModelProperty("故障信息")
      private String faultInfo;

      @ApiModelProperty("故障地区")
      private String area;

      @ApiModelProperty("故障具体位置")
      private String location;

      @ApiModelProperty("创建时间")
      private LocalDateTime createTime;

      @ApiModelProperty("上报人电话")
      private String phone;

  @ApiModelProperty("故障单状态")
  private Integer status;

  @ApiModelProperty("故障单状态")
  private Integer faultParts;

  @ApiModelProperty("当前处理人")
  private String dealPerson;

  @ApiModelProperty("网格编号")
  private Integer gridId;

  @ApiModelProperty("开始时间")
  private LocalDateTime startTime;

  @ApiModelProperty("结束时间")
  private LocalDateTime endTime;

  @ApiModelProperty("预期结束时间")
  private LocalDateTime expectTime;

  @ApiModelProperty("处理时长")
  private String handleTime;

  @ApiModelProperty("是否逾期")
  private Integer ifLate;


}
