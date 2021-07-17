package org.group.mall.controller.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 拼团订单
 */

@Data
public class GroupWorkOrderUVO implements Serializable {

  private Long cartItemId;

  private Long userId;

  private Long groupId;

  private Integer goodsCount;

  private String groupName;

  private String goodsCoverImg;

  private Integer groupPrice;

}