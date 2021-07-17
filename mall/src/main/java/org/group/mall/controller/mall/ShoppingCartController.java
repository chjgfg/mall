package org.group.mall.controller.mall;

import org.group.mall.common.Constants;
import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.ShoppingCartItemVO;
import org.group.mall.controller.vo.UserVO;
import org.group.mall.entity.ShoppingCartItem;
import org.group.mall.service.ShoppingCartService;
import org.group.mall.util.Result;
import org.group.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

  @Resource
  private ShoppingCartService shoppingCartService;

  @GetMapping("/shop-cart")
  public String cartListPage(HttpServletRequest request, HttpSession httpSession) {
    UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
    int itemsTotal = 0;
    int priceTotal = 0;
    List<ShoppingCartItemVO> myShoppingCartItems = shoppingCartService.getMyShoppingCartItems(user.getUserId());
    if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
      //购物项总数
      itemsTotal = myShoppingCartItems.stream().mapToInt(ShoppingCartItemVO::getGoodsCount).sum();
      if (itemsTotal < 1) {
        return "error/error_5xx";
      }
      //总价
      for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
        priceTotal += shoppingCartItemVO.getGoodsCount() * shoppingCartItemVO.getSellingPrice();
      }
      if (priceTotal < 1) {
        return "error/error_5xx";
      }
    }
    request.setAttribute("itemsTotal", itemsTotal);
    request.setAttribute("priceTotal", priceTotal);
    request.setAttribute("myShoppingCartItems", myShoppingCartItems);
    return "mall/cart";
  }

  @PostMapping("/shop-cart")
  @ResponseBody
  public Result saveNewBeeMallShoppingCartItem(@RequestBody ShoppingCartItem shoppingCartItem, HttpSession httpSession) {
    UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
    shoppingCartItem.setUserId(user.getUserId());
    //todo 判断数量
    String saveResult = shoppingCartService.saveNewBeeMallCartItem(shoppingCartItem);
//    System.out.println("shoppingCartItem = " + shoppingCartItem);
    //添加成功
    if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
      return ResultGenerator.genSuccessResult();
    }
    //添加失败
    return ResultGenerator.genFailResult(saveResult);
  }

  @PutMapping("/shop-cart")
  @ResponseBody
  public Result updateNewBeeMallShoppingCartItem(@RequestBody ShoppingCartItem shoppingCartItem,
                                                 HttpSession httpSession) {
    UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
    shoppingCartItem.setUserId(user.getUserId());
    //todo 判断数量
    String updateResult = shoppingCartService.updateNewBeeMallCartItem(shoppingCartItem);
    //修改成功
    if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
      return ResultGenerator.genSuccessResult();
    }
    //修改失败
    return ResultGenerator.genFailResult(updateResult);
  }

  @DeleteMapping("/shop-cart/{newBeeMallShoppingCartItemId}")
  @ResponseBody
  public Result updateNewBeeMallShoppingCartItem(@PathVariable("newBeeMallShoppingCartItemId") Long newBeeMallShoppingCartItemId,
                                                 HttpSession httpSession) {
    UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
    Boolean deleteResult = shoppingCartService.deleteById(newBeeMallShoppingCartItemId);
    //删除成功
    if (deleteResult) {
      return ResultGenerator.genSuccessResult();
    }
    //删除失败
    return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
  }

  @GetMapping("/shop-cart/settle")
  public String settlePage(HttpServletRequest request,
                           HttpSession httpSession) {
    int priceTotal = 0;
    UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
    List<ShoppingCartItemVO> myShoppingCartItems = shoppingCartService.getMyShoppingCartItems(user.getUserId());
    if (CollectionUtils.isEmpty(myShoppingCartItems)) {
      //无数据则不跳转至结算页
      return "/shop-cart";
    } else {
      //总价
      for (ShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
        priceTotal += shoppingCartItemVO.getGoodsCount() * shoppingCartItemVO.getSellingPrice();
      }
      if (priceTotal < 1) {
        return "error/error_5xx";
      }
    }
    request.setAttribute("priceTotal", priceTotal);
    request.setAttribute("myShoppingCartItems", myShoppingCartItems);
    return "mall/order-settle";
  }
}
