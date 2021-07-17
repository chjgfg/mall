package org.group.mall.common;

public enum ServiceResultEnum {
  ERROR("error"),

  SUCCESS("success"),

  //  GROUP_EXIT_JOIN("您已经加入该队伍"),
//
//  GROUP_WAIT("请等待拼单人数足够"),
//
//  GROUP_SUCCESS("拼单成功"),
//
//  GROUP_NOT_EXIST("该拼单队伍不存在！"),
//
//  DATA_NOT_EXIST("未查询到记录！"),
//
//  SAME_CATEGORY_EXIST("有同级同名的分类！"),
//
//  SAME_LOGIN_NAME_EXIST("用户名已存在！"),
//
//  LOGIN_NAME_NULL("请输入登录名！"),
//
//  LOGIN_PASSWORD_NULL("请输入密码！"),
//
//  LOGIN_VERIFY_CODE_NULL("请输入验证码！"),
//
//  LOGIN_VERIFY_CODE_ERROR("验证码错误！"),
//
//  GOODS_NOT_EXIST("商品不存在！"),
//
//  GOODS_PUT_DOWN("商品已下架！"),
//
//  SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR("超出单个商品的最大购买数量！"),
//
//  SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR("超出购物车最大容量！"),
//
//  LOGIN_ERROR("登录失败！"),
//
//  LOGIN_USER_LOCKED("用户已被禁止登录！"),
//
//  ORDER_NOT_EXIST_ERROR("订单不存在！"),
//
//  NULL_ADDRESS_ERROR("地址不能为空！"),
//
//  ORDER_PRICE_ERROR("订单价格异常！"),
//
//  ORDER_GENERATE_ERROR("生成订单异常！"),
//
//  SHOPPING_ITEM_ERROR("购物车数据异常！"),
//
//  SHOPPING_ITEM_COUNT_ERROR("库存不足！"),
//
//  ORDER_STATUS_ERROR("订单状态异常！"),
//
//  OPERATE_ERROR("操作失败！"),
  GROUP_EXIT_JOIN("You have joined the team"),

  GROUP_WAIT("Please wait for enough people"),

  GROUP_SUCCESS("Successfully put together"),

  GROUP_NOT_EXIST("The team does not exist!"),

  DATA_NOT_EXIST("No record found!"),

  SAME_CATEGORY_EXIST("There are categories with the same name and the same name!"),

  SAME_LOGIN_NAME_EXIST("Username already exists!"),

  LOGIN_NAME_NULL("Please enter your login name!"),

  LOGIN_PASSWORD_NULL("Please enter password!"),

  LOGIN_VERIFY_CODE_NULL("please enter verification code!"),

  LOGIN_VERIFY_CODE_ERROR("Verification code error!"),

  GOODS_NOT_EXIST("Product does not exist!"),

  GOODS_PUT_DOWN("Product has been removed!"),

  SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR("Exceed the maximum purchase quantity of a single product!"),

  SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR("Exceeded the maximum capacity of the shopping cart!"),

  LOGIN_ERROR("Login failed!"),

  LOGIN_USER_LOCKED("User has been banned from logging in!"),

  ORDER_NOT_EXIST_ERROR("Order does not exist!"),

  NULL_ADDRESS_ERROR("Address cannot be empty!"),

  ORDER_PRICE_ERROR("Order price is abnormal!"),

  ORDER_GENERATE_ERROR("Generate order exception!"),

  SHOPPING_ITEM_ERROR("Shopping cart data is abnormal!"),

  SHOPPING_ITEM_COUNT_ERROR("Inventory shortage!"),

  ORDER_STATUS_ERROR("Abnormal order status!"),

  OPERATE_ERROR("operation failed!"),


  DB_ERROR("database error");

  private String result;

  ServiceResultEnum(String result) {
    this.result = result;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }
}
