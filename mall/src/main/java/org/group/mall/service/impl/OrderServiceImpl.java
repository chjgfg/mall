package org.group.mall.service.impl;

import org.group.mall.common.Constants;
import org.group.mall.common.MyException;
import org.group.mall.common.OrderStatusEnum;
import org.group.mall.common.PayStatusEnum;
import org.group.mall.common.PayTypeEnum;
import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.NewBeeMallOrderItemVO;
import org.group.mall.controller.vo.OrderDetailVO;
import org.group.mall.controller.vo.OrderListVO;
import org.group.mall.controller.vo.ShoppingCartItemVO;
import org.group.mall.controller.vo.UserVO;
import org.group.mall.dao.GoodsMapper;
import org.group.mall.dao.OrderItemMapper;
import org.group.mall.dao.OrderMapper;
import org.group.mall.dao.ShoppingCartItemMapper;
import org.group.mall.entity.Goods;
import org.group.mall.entity.Order;
import org.group.mall.entity.OrderItem;
import org.group.mall.entity.StockNumDTO;
import org.group.mall.service.OrderService;
import org.group.mall.util.BeanUtil;
import org.group.mall.util.NumberUtil;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class OrderServiceImpl implements OrderService {

  @Autowired
  private OrderMapper orderMapper;
  @Autowired
  private OrderItemMapper orderItemMapper;
  @Autowired
  private ShoppingCartItemMapper shoppingCartItemMapper;
  @Autowired
  private GoodsMapper goodsMapper;

  @Override
  public PageResult getNewBeeMallOrdersPage(PageQueryUtil pageUtil) {
    List<Order> orders = orderMapper.findNewBeeMallOrderList(pageUtil);
    int total = orderMapper.getTotalNewBeeMallOrders(pageUtil);
    PageResult pageResult = new PageResult(orders, total, pageUtil.getLimit(), pageUtil.getPage());
    return pageResult;
  }

  @Override
  @Transactional
  public String updateOrderInfo(Order order) {
    Order temp = orderMapper.selectByPrimaryKey(order.getOrderId());
    //不为空且orderStatus>=0且状态为出库之前可以修改部分信息
    if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
      temp.setTotalPrice(order.getTotalPrice());
      temp.setUserAddress(order.getUserAddress());
      temp.setUpdateTime(new Date());
      if (orderMapper.updateByPrimaryKeySelective(temp) > 0) {
        return ServiceResultEnum.SUCCESS.getResult();
      }
      return ServiceResultEnum.DB_ERROR.getResult();
    }
    return ServiceResultEnum.DATA_NOT_EXIST.getResult();
  }

  @Override
  @Transactional
  public String checkDone(Long[] ids) {
    //查询所有的订单 判断状态 修改状态和更新时间
    List<Order> orders = orderMapper.selectByPrimaryKeys(Arrays.asList(ids));
    String errorOrderNos = "";
    if (!CollectionUtils.isEmpty(orders)) {
      for (Order order : orders) {
        if (order.getIsDeleted() == 1) {
          errorOrderNos += order.getOrderNo() + " ";
          continue;
        }
        if (order.getOrderStatus() != 1) {
          errorOrderNos += order.getOrderNo() + " ";
        }
      }
      if (StringUtils.isEmpty(errorOrderNos)) {
        //订单状态正常 可以执行配货完成操作 修改订单状态和更新时间
        if (orderMapper.checkDone(Arrays.asList(ids)) > 0) {
          return ServiceResultEnum.SUCCESS.getResult();
        } else {
          return ServiceResultEnum.DB_ERROR.getResult();
        }
      } else {
        //订单此时不可执行出库操作
        if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
          return errorOrderNos + "订单的状态不是支付成功无法执行出库操作";
        } else {
          return "你选择了太多状态不是支付成功的订单，无法执行配货完成操作";
        }
      }
    }
    //未查询到数据 返回错误提示
    return ServiceResultEnum.DATA_NOT_EXIST.getResult();
  }

  @Override
  @Transactional
  public String checkOut(Long[] ids) {
    //查询所有的订单 判断状态 修改状态和更新时间
    List<Order> orders = orderMapper.selectByPrimaryKeys(Arrays.asList(ids));
    String errorOrderNos = "";
    if (!CollectionUtils.isEmpty(orders)) {
      for (Order order : orders) {
        if (order.getIsDeleted() == 1) {
          errorOrderNos += order.getOrderNo() + " ";
          continue;
        }
        if (order.getOrderStatus() != 1 && order.getOrderStatus() != 2) {
          errorOrderNos += order.getOrderNo() + " ";
        }
      }
      if (StringUtils.isEmpty(errorOrderNos)) {
        //订单状态正常 可以执行出库操作 修改订单状态和更新时间
        if (orderMapper.checkOut(Arrays.asList(ids)) > 0) {
          return ServiceResultEnum.SUCCESS.getResult();
        } else {
          return ServiceResultEnum.DB_ERROR.getResult();
        }
      } else {
        //订单此时不可执行出库操作
        if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
          return errorOrderNos + "订单的状态不是支付成功或配货完成无法执行出库操作";
        } else {
          return "你选择了太多状态不是支付成功或配货完成的订单，无法执行出库操作";
        }
      }
    }
    //未查询到数据 返回错误提示
    return ServiceResultEnum.DATA_NOT_EXIST.getResult();
  }

  @Override
  @Transactional
  public String closeOrder(Long[] ids) {
    //查询所有的订单 判断状态 修改状态和更新时间
    List<Order> orders = orderMapper.selectByPrimaryKeys(Arrays.asList(ids));
    String errorOrderNos = "";
    if (!CollectionUtils.isEmpty(orders)) {
      for (Order order : orders) {
        // isDeleted=1 一定为已关闭订单
        if (order.getIsDeleted() == 1) {
          errorOrderNos += order.getOrderNo() + " ";
          continue;
        }
        //已关闭或者已完成无法关闭订单
        if (order.getOrderStatus() == 4 || order.getOrderStatus() < 0) {
          errorOrderNos += order.getOrderNo() + " ";
        }
      }
      if (StringUtils.isEmpty(errorOrderNos)) {
        //订单状态正常 可以执行关闭操作 修改订单状态和更新时间
        if (orderMapper.closeOrder(Arrays.asList(ids), OrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
          return ServiceResultEnum.SUCCESS.getResult();
        } else {
          return ServiceResultEnum.DB_ERROR.getResult();
        }
      } else {
        //订单此时不可执行关闭操作
        if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
          return errorOrderNos + "订单不能执行关闭操作";
        } else {
          return "你选择的订单不能执行关闭操作";
        }
      }
    }
    //未查询到数据 返回错误提示
    return ServiceResultEnum.DATA_NOT_EXIST.getResult();
  }

  @Override
  @Transactional
  public String saveOrder(UserVO user, List<ShoppingCartItemVO> myShoppingCartItems) {
    List<Long> itemIdList = myShoppingCartItems.stream().map(ShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
    List<Long> goodsIds = myShoppingCartItems.stream().map(ShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
    List<Goods> goods = goodsMapper.selectByPrimaryKeys(goodsIds);
    //检查是否包含已下架商品
    List<Goods> goodsListNotSelling = goods.stream()
        .filter(newBeeMallGoodsTemp -> newBeeMallGoodsTemp.getGoodsSellStatus() != Constants.SELL_STATUS_UP)
        .collect(Collectors.toList());//Collectors.toList()用来结束Stream流。
    if (!CollectionUtils.isEmpty(goodsListNotSelling)) {
      //goodsListNotSelling 对象非空则表示有下架商品
      MyException.fail(goodsListNotSelling.get(0).getGoodsName() + "已下架，无法生成订单");
    }
    Map<Long, Goods> newBeeMallGoodsMap = goods.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));//Collectors.toMap 根据收集自身对象,.根据 getGoodsId 和 对象自己 Function.identity() 转成 Map 集合
    //判断商品库存
    for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
      //查出的商品中不存在购物车中的这条关联商品数据，直接返回错误提醒
      if (!newBeeMallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
        MyException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
      }
      //存在数量大于库存的情况，直接返回错误提醒
      if (shoppingCartItemVO.getGoodsCount() > newBeeMallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
        MyException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
      }
    }
    //删除购物项
    if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(goods)) {
      if (shoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
        List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
        int updateStockNumResult = goodsMapper.updateStockNum(stockNumDTOS);
        if (updateStockNumResult < 1) {
          MyException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
        }
        //生成订单号
        String orderNo = NumberUtil.genOrderNo();
        int priceTotal = 0;
        //保存订单
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(user.getUserId());
        order.setUserAddress(user.getAddress());
        //总价
        for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
          priceTotal += shoppingCartItemVO.getGoodsCount() * shoppingCartItemVO.getSellingPrice();
        }
        if (priceTotal < 1) {
          MyException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
        }
        order.setTotalPrice(priceTotal);
        //todo 订单body字段，用来作为生成支付单描述信息，暂时未接入第三方支付接口，故该字段暂时设为空字符串
        String extraInfo = "";
        order.setExtraInfo(extraInfo);
        //生成订单项并保存订单项纪录
        if (orderMapper.insertSelective(order) > 0) {
          //生成所有的订单项快照，并保存至数据库
          List<OrderItem> orderItems = new ArrayList<>();
          for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            OrderItem orderItem = new OrderItem();
            //使用BeanUtil工具类将newBeeMallShoppingCartItemVO中的属性复制到newBeeMallOrderItem对象中
            BeanUtil.copyProperties(shoppingCartItemVO, orderItem);
            //NewBeeMallOrderMapper文件insert()方法中使用了useGeneratedKeys因此orderId可以获取到
            orderItem.setOrderId(order.getOrderId());
            orderItems.add(orderItem);
          }
          //保存至数据库
          if (orderItemMapper.insertBatch(orderItems) > 0) {
            //所有操作成功后，将订单号返回，以供Controller方法跳转到订单详情
            return orderNo;
          }
          MyException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
        }
        MyException.fail(ServiceResultEnum.DB_ERROR.getResult());
      }
      MyException.fail(ServiceResultEnum.DB_ERROR.getResult());
    }
    MyException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
    return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
  }

  @Override
  public OrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
    Order order = orderMapper.selectByOrderNo(orderNo);
    if (order != null) {
      //todo 验证是否是当前userId下的订单，否则报错
      List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getOrderId());
      //获取订单项数据
      if (!CollectionUtils.isEmpty(orderItems)) {
        List<NewBeeMallOrderItemVO> newBeeMallOrderItemVOS = BeanUtil.copyList(orderItems, NewBeeMallOrderItemVO.class);
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        BeanUtil.copyProperties(order, orderDetailVO);
        orderDetailVO.setOrderStatusString(OrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(orderDetailVO.getOrderStatus()).getName());
        orderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(orderDetailVO.getPayType()).getName());
        orderDetailVO.setNewBeeMallOrderItemVOS(newBeeMallOrderItemVOS);
        return orderDetailVO;
      }
    }
    return null;
  }

  @Override
  public Order getNewBeeMallOrderByOrderNo(String orderNo) {
    return orderMapper.selectByOrderNo(orderNo);
  }

  @Override
  public PageResult getMyOrders(PageQueryUtil pageUtil) {
    int total = orderMapper.getTotalNewBeeMallOrders(pageUtil);
    List<Order> orders = orderMapper.findNewBeeMallOrderList(pageUtil);
    List<OrderListVO> orderListVOS = new ArrayList<>();
    if (total > 0) {
      //数据转换 将实体类转成vo
      orderListVOS = BeanUtil.copyList(orders, OrderListVO.class);
      //设置订单状态中文显示值
      for (OrderListVO orderListVO : orderListVOS) {
        orderListVO.setOrderStatusString(OrderStatusEnum.getNewBeeMallOrderStatusEnumByStatus(orderListVO.getOrderStatus()).getName());
      }
      List<Long> orderIds = orders.stream().map(Order::getOrderId).collect(Collectors.toList());
      if (!CollectionUtils.isEmpty(orderIds)) {
        List<OrderItem> orderItems = orderItemMapper.selectByOrderIds(orderIds);
        Map<Long, List<OrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(OrderItem::getOrderId));
        for (OrderListVO orderListVO : orderListVOS) {
          //封装每个订单列表对象的订单项数据
          if (itemByOrderIdMap.containsKey(orderListVO.getOrderId())) {
            List<OrderItem> orderItemListTemp = itemByOrderIdMap.get(orderListVO.getOrderId());
            //将NewBeeMallOrderItem对象列表转换成NewBeeMallOrderItemVO对象列表
            List<NewBeeMallOrderItemVO> newBeeMallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, NewBeeMallOrderItemVO.class);
            orderListVO.setNewBeeMallOrderItemVOS(newBeeMallOrderItemVOS);
          }
        }
      }
    }
    PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
    return pageResult;
  }

  @Override
  public String cancelOrder(String orderNo, Long userId) {
    Order order = orderMapper.selectByOrderNo(orderNo);
    if (order != null) {
      //todo 验证是否是当前userId下的订单，否则报错
      //todo 订单状态判断
      if (orderMapper.closeOrder(Collections.singletonList(order.getOrderId()), OrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
        return ServiceResultEnum.SUCCESS.getResult();
      } else {
        return ServiceResultEnum.DB_ERROR.getResult();
      }
    }
    return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
  }

  @Override
  public String finishOrder(String orderNo, Long userId) {
    Order order = orderMapper.selectByOrderNo(orderNo);
    if (order != null) {
      //todo 验证是否是当前userId下的订单，否则报错
      //todo 订单状态判断
      order.setOrderStatus((byte) OrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
      order.setUpdateTime(new Date());
      if (orderMapper.updateByPrimaryKeySelective(order) > 0) {
        return ServiceResultEnum.SUCCESS.getResult();
      } else {
        return ServiceResultEnum.DB_ERROR.getResult();
      }
    }
    return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
  }

  @Override
  public String paySuccess(String orderNo, int payType) {
    Order order = orderMapper.selectByOrderNo(orderNo);
    if (order != null) {
      //todo 订单状态判断 非待支付状态下不进行修改操作
      order.setOrderStatus((byte) OrderStatusEnum.OREDER_PAID.getOrderStatus());
      order.setPayType((byte) payType);
      order.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
      order.setPayTime(new Date());
      order.setUpdateTime(new Date());
      if (orderMapper.updateByPrimaryKeySelective(order) > 0) {
        return ServiceResultEnum.SUCCESS.getResult();
      } else {
        return ServiceResultEnum.DB_ERROR.getResult();
      }
    }
    return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
  }

  @Override
  public List<NewBeeMallOrderItemVO> getOrderItems(Long id) {
    Order order = orderMapper.selectByPrimaryKey(id);
    if (order != null) {
      List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getOrderId());
      //获取订单项数据
      if (!CollectionUtils.isEmpty(orderItems)) {
        List<NewBeeMallOrderItemVO> newBeeMallOrderItemVOS = BeanUtil.copyList(orderItems, NewBeeMallOrderItemVO.class);
        return newBeeMallOrderItemVOS;
      }
    }
    return null;
  }
}