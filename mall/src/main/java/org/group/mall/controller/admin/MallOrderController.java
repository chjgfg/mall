package org.group.mall.controller.admin;

import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.NewBeeMallOrderItemVO;
import org.group.mall.entity.Order;
import org.group.mall.service.OrderService;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.Result;
import org.group.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class MallOrderController {

  @Resource
  private OrderService orderService;

  @GetMapping("/orders")
  public String ordersPage(HttpServletRequest request) {
    request.setAttribute("path", "orders");
    return "admin/mall_order";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/orders/list", method = RequestMethod.GET)
  @ResponseBody
  public Result list(@RequestParam Map<String, Object> params) {
    if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    PageQueryUtil pageUtil = new PageQueryUtil(params);
    return ResultGenerator.genSuccessResult(orderService.getNewBeeMallOrdersPage(pageUtil));
  }

  /**
   * 修改
   */
  @RequestMapping(value = "/orders/update", method = RequestMethod.POST)
  @ResponseBody
  public Result update(@RequestBody Order newBeeMallOrder) {
    if (Objects.isNull(newBeeMallOrder.getTotalPrice())
        || Objects.isNull(newBeeMallOrder.getOrderId())
        || newBeeMallOrder.getOrderId() < 1
        || newBeeMallOrder.getTotalPrice() < 1
        || StringUtils.isEmpty(newBeeMallOrder.getUserAddress())) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = orderService.updateOrderInfo(newBeeMallOrder);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }

  /**
   * 详情
   */
  @GetMapping("/order-items/{id}")
  @ResponseBody
  public Result info(@PathVariable("id") Long id) {
    List<NewBeeMallOrderItemVO> orderItems = orderService.getOrderItems(id);
    if (!CollectionUtils.isEmpty(orderItems)) {
      return ResultGenerator.genSuccessResult(orderItems);
    }
    return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
  }

  /**
   * 配货
   */
  @RequestMapping(value = "/orders/checkDone", method = RequestMethod.POST)
  @ResponseBody
  public Result checkDone(@RequestBody Long[] ids) {
    if (ids.length < 1) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = orderService.checkDone(ids);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }

  /**
   * 出库
   */
  @RequestMapping(value = "/orders/checkOut", method = RequestMethod.POST)
  @ResponseBody
  public Result checkOut(@RequestBody Long[] ids) {
    if (ids.length < 1) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = orderService.checkOut(ids);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }

  /**
   * 关闭订单
   */
  @RequestMapping(value = "/orders/close", method = RequestMethod.POST)
  @ResponseBody
  public Result closeOrder(@RequestBody Long[] ids) {
    if (ids.length < 1) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = orderService.closeOrder(ids);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }


}
