package org.group.mall.dao;

import org.group.mall.entity.ShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShoppingCartItemMapper {
  int deleteByPrimaryKey(Long cartItemId);

  int insert(ShoppingCartItem record);

  int insertSelective(ShoppingCartItem record);

  ShoppingCartItem selectByPrimaryKey(Long cartItemId);

  ShoppingCartItem selectByUserIdAndGoodsId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("goodsId") Long goodsId);

  List<ShoppingCartItem> selectByUserId(@Param("newBeeMallUserId") Long newBeeMallUserId, @Param("number") int number);

  int selectCountByUserId(Long newBeeMallUserId);

  int updateByPrimaryKeySelective(ShoppingCartItem record);

  int updateByPrimaryKey(ShoppingCartItem record);

  int deleteBatch(List<Long> ids);
}