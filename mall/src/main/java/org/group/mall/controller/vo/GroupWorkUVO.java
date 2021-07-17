package org.group.mall.controller.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 拼单商品
 */
@Data
public class GroupWorkUVO implements Serializable {

  //拼单商品id
  private Integer groupWorkId;

  //拼团队伍
  private List<GroupUVO> groupUVOS;

}