package org.group.mall.controller.vo;

public class GroupSaveExistVO {

  private Long goodsId;

  private Integer groupNum;

  private Integer groupPrice;

  private Integer groupSellStatus;

  public void setGoodsId(Long goodsId) {
    this.goodsId = goodsId;
  }

  public void setGroupNum(Integer groupNum) {
    this.groupNum = groupNum;
  }

  public void setGroupPrice(Integer groupPrice) {
    this.groupPrice = groupPrice;
  }

  public void setGroupSellStatus(Integer groupSellStatus) {
    this.groupSellStatus = groupSellStatus;
  }

  public Long getGoodsId() {
    return goodsId;
  }

  public Integer getGroupNum() {
    return groupNum;
  }

  public Integer getGroupPrice() {
    return groupPrice;
  }

  public Integer getGroupSellStatus() {
    return groupSellStatus;
  }
}
