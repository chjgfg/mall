package org.group.mall.dao;

import org.group.mall.entity.GroupWork;
import org.group.mall.entity.StockNumDTO;
import org.group.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface GroupWorkUMapper {

  @Select("select * from tb_newbee_mall_group_work where group_Id= #{groupId}")
  GroupWork selectGroupWorkById(Long groupId);

  @Select("select count(*) from tb_newbee_mall_shopping_cart_item where user_id = #{userId,jdbcType=BIGINT} and is_deleted = 0")
  int selectCountByUserId(Long userId);

  @Select({"<script> " +
      "select * from tb_newbee_mall_group_work" +
      "<where>" +
      "<if test='keyword!=null'> and (group_name like CONCAT('%',#{keyword},'%') or group_intro like CONCAT('%',#{keyword},'%'))</if>" +
      "<if test='groupSellStatus!=null'>and group_sell_status = #{groupSellStatus}</if>" +
      "</where>" +
      "<if test='orderBy!=null'>" +
      "<choose>" +
      "<when test='orderBy == new'>order by group_id desc</when>" +
      "<when test='orderBy == price'>order by group_price asc</when>" +
      "<otherwise>order by stock_num desc</otherwise>" +
      "</choose>" +
      "</if>" +
      "<if test='start!=null and limit!=null'> limit #{start},#{limit}</if>" +
      "</script>"})
  List<GroupWork> findGroupWorkListBySearch(PageQueryUtil pageUtil);

  @Select({"<script> " +
      "select count(*) from tb_newbee_mall_group_work " +
      "<where>" +
      "<if test='keyword!=null'>and (group_name like CONCAT('%',#{keyword},'%') or group_intro like CONCAT('%',#{keyword},'%'))</if>" +
      "<if test='groupSellStatus!=null'> and group_sell_status = #{groupSellStatus}</if>" +
      "</where>" +
      "</script>"})
  int getTotalGroupWorkBySearch(PageQueryUtil pageUtil);

  @Update({"<script> " +
      "<foreach collection='stockNumDTOS' item='stockNumDTO'> " +
      "update tb_newbee_mall_group_work set stock_num = stock_num-#{stockNumDTO.goodsCount}" +
      "where group_id = #{stockNumDTO.goodsId} and stock_num>=#{stockNumDTO.goodsCount} and group_sell_status = 0;" +
      "</foreach>" +
      "</script>"})
  int updateGroupNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);


  @Select("select * from tb_newbee_mall_group_work  order by rand() limit #{number,jdbcType=BIGINT};")
  List<GroupWork> findSome(long number);

}
