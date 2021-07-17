package org.group.mall.service;

import org.group.mall.controller.vo.ShoppingCartItemVO;
import org.group.mall.entity.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartService {

  /**
   * 保存商品至购物车中
   *
   * @param shoppingCartItem
   * @return
   */
  String saveNewBeeMallCartItem(ShoppingCartItem shoppingCartItem);

  /**
   * 修改购物车中的属性
   *
   * @param shoppingCartItem
   * @return
   */
  String updateNewBeeMallCartItem(ShoppingCartItem shoppingCartItem);

  /**
   * 获取购物项详情
   *
   * @param newBeeMallShoppingCartItemId
   * @return
   */
  ShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId);

  /**
   * 删除购物车中的商品
   *
   * @param newBeeMallShoppingCartItemId
   * @return
   */
  Boolean deleteById(Long newBeeMallShoppingCartItemId);

  /**
   * 获取我的购物车中的列表数据
   *
   * @param newBeeMallUserId
   * @return
   */
  List<ShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId);
}
