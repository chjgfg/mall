package org.group.mall.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 拼团队伍
 */
@Data
public class GroupUVO implements Serializable {

  //拼单队伍id --uuid
  private String groupId;

  //拼单订单详情
  private List<GroupWorkOrderUVO> groupWorkOrder;

  //目前队伍人数
  private Integer currentNum;

  //拼团成功所需人数
  private Integer groupNum;

  //到期时间
  private Date endTime;
}