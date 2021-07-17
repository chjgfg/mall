package org.group.mall.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupWork implements Serializable {

  private Long group_id;

  private String group_name;

  private String group_intro;

  private Long group_category_id;

  private String group_cover_img;

  private String group_detail_content;

  private Integer original_price;

  private Integer stock_num;

  private String tag;

  private Integer group_num;

  private Integer group_price;

  private Integer group_sell_status;

}