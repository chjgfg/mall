package org.group.mall.service;

import org.group.mall.controller.vo.OrderDetailVO;
import org.group.mall.controller.vo.NewBeeMallOrderItemVO;
import org.group.mall.controller.vo.ShoppingCartItemVO;
import org.group.mall.controller.vo.UserVO;
import org.group.mall.entity.Order;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;

import java.util.List;

public interface OrderService {
  /**
   * 后台分页
   *
   * @param pageUtil
   * @return
   */
  PageResult getNewBeeMallOrdersPage(PageQueryUtil pageUtil);

  /**
   * 订单信息修改
   *
   * @param order
   * @return
   */
  String updateOrderInfo(Order order);

  /**
   * 配货
   *
   * @param ids
   * @return
   */
  String checkDone(Long[] ids);

  /**
   * 出库
   *
   * @param ids
   * @return
   */
  String checkOut(Long[] ids);

  /**
   * 关闭订单
   *
   * @param ids
   * @return
   */
  String closeOrder(Long[] ids);

  /**
   * 保存订单
   *
   * @param user
   * @param myShoppingCartItems
   * @return
   */
  String saveOrder(UserVO user, List<ShoppingCartItemVO> myShoppingCartItems);

  /**
   * 获取订单详情
   *
   * @param orderNo
   * @param userId
   * @return
   */
  OrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

  /**
   * 获取订单详情
   *
   * @param orderNo
   * @return
   */
  Order getNewBeeMallOrderByOrderNo(String orderNo);

  /**
   * 我的订单列表
   *
   * @param pageUtil
   * @return
   */
  PageResult getMyOrders(PageQueryUtil pageUtil);

  /**
   * 手动取消订单
   *
   * @param orderNo
   * @param userId
   * @return
   */
  String cancelOrder(String orderNo, Long userId);

  /**
   * 确认收货
   *
   * @param orderNo
   * @param userId
   * @return
   */
  String finishOrder(String orderNo, Long userId);

  String paySuccess(String orderNo, int payType);

  List<NewBeeMallOrderItemVO> getOrderItems(Long id);
}
