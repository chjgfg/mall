package org.group.mall.entity;


public class MallGroup {

  private Long groupId;

  private String groupName;

  private String groupIntro;

  private Long groupCategoryId;

  private String groupCoverImg;

  private String groupDetailContent;

  private Integer originalPrice;

  private Integer stockNum;

  private String tag;


  private Integer groupNum;

  private Integer groupPrice;

  private Integer groupSellStatus;

  public void setGroupSellStatus(Integer groupSellStatus) {
    this.groupSellStatus = groupSellStatus;
  }

  public Integer getGroupSellStatus() {
    return groupSellStatus;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public void setGroupIntro(String groupIntro) {
    this.groupIntro = groupIntro;
  }

  public void setGroupCategoryId(Long groupCategoryId) {
    this.groupCategoryId = groupCategoryId;
  }

  public void setGroupCoverImg(String groupCoverImg) {
    this.groupCoverImg = groupCoverImg;
  }

  public void setGroupDetailContent(String groupDetailContent) {
    this.groupDetailContent = groupDetailContent;
  }

  public void setOriginalPrice(Integer originalPrice) {
    this.originalPrice = originalPrice;
  }

  public void setStockNum(Integer stockNum) {
    this.stockNum = stockNum;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setGroupNum(Integer groupNum) {
    this.groupNum = groupNum;
  }

  public void setGroupPrice(Integer groupPrice) {
    this.groupPrice = groupPrice;
  }


  public Long getGroupId() {
    return groupId;
  }

  public String getGroupName() {
    return groupName;
  }

  public String getGroupIntro() {
    return groupIntro;
  }

  public Long getGroupCategoryId() {
    return groupCategoryId;
  }

  public String getGroupCoverImg() {
    return groupCoverImg;
  }

  public String getGroupDetailContent() {
    return groupDetailContent;
  }

  public Integer getOriginalPrice() {
    return originalPrice;
  }

  public Integer getStockNum() {
    return stockNum;
  }

  public String getTag() {
    return tag;
  }

  public Integer getGroupNum() {
    return groupNum;
  }

  public Integer getGroupPrice() {
    return groupPrice;
  }


  @Override
  public String toString() {
    return "MallGroup{" +
        "groupId=" + groupId +
        ", groupName='" + groupName + '\'' +
        ", groupIntro='" + groupIntro + '\'' +
        ", groupCategoryId=" + groupCategoryId +
        ", groupCoverImg='" + groupCoverImg + '\'' +
        ", groupDetailContent='" + groupDetailContent + '\'' +
        ", originalPrice=" + originalPrice +
        ", stockNum=" + stockNum +
        ", tag='" + tag + '\'' +
        ", groupNum=" + groupNum +
        ", groupPrice=" + groupPrice +
        '}';
  }
}
