package org.group.mall.service.impl;

import org.group.mall.common.Constants;
import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.ShoppingCartItemVO;
import org.group.mall.dao.GoodsMapper;
import org.group.mall.dao.ShoppingCartItemMapper;
import org.group.mall.entity.Goods;
import org.group.mall.entity.ShoppingCartItem;
import org.group.mall.service.ShoppingCartService;
import org.group.mall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

  @Autowired
  private ShoppingCartItemMapper shoppingCartItemMapper;

  @Autowired
  private GoodsMapper goodsMapper;

  //todo 修改session中购物项数量

  @Override
  public String saveNewBeeMallCartItem(ShoppingCartItem shoppingCartItem) {
    ShoppingCartItem temp = shoppingCartItemMapper.selectByUserIdAndGoodsId(shoppingCartItem.getUserId(), shoppingCartItem.getGoodsId());
    if (temp != null) {
      //已存在则修改该记录
      //todo count = tempCount + 1
      temp.setGoodsCount(shoppingCartItem.getGoodsCount());
      return updateNewBeeMallCartItem(temp);
    }
    Goods goods = goodsMapper.selectByPrimaryKey(shoppingCartItem.getGoodsId());
    //商品为空
    if (goods == null) {
      return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
    }
    int totalItem = shoppingCartItemMapper.selectCountByUserId(shoppingCartItem.getUserId()) + 1;
    //超出单个商品的最大数量
    if (shoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
      return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
    }
    //超出最大数量
    if (totalItem > Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER) {
      return ServiceResultEnum.SHOPPING_CART_ITEM_TOTAL_NUMBER_ERROR.getResult();
    }
    //保存记录
    if (shoppingCartItemMapper.insertSelective(shoppingCartItem) > 0) {
      return ServiceResultEnum.SUCCESS.getResult();
    }
    return ServiceResultEnum.DB_ERROR.getResult();
  }

  @Override
  public String updateNewBeeMallCartItem(ShoppingCartItem shoppingCartItem) {
    ShoppingCartItem shoppingCartItemUpdate = shoppingCartItemMapper.selectByPrimaryKey(shoppingCartItem.getCartItemId());
    if (shoppingCartItemUpdate == null) {
      return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }
    //超出单个商品的最大数量
    if (shoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
      return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
    }
    //todo 数量相同不会进行修改
    //todo userId不同不能修改
    shoppingCartItemUpdate.setGoodsCount(shoppingCartItem.getGoodsCount());
    shoppingCartItemUpdate.setUpdateTime(new Date());
    //修改记录
    if (shoppingCartItemMapper.updateByPrimaryKeySelective(shoppingCartItemUpdate) > 0) {
      return ServiceResultEnum.SUCCESS.getResult();
    }
    return ServiceResultEnum.DB_ERROR.getResult();
  }

  @Override
  public ShoppingCartItem getNewBeeMallCartItemById(Long newBeeMallShoppingCartItemId) {
    return shoppingCartItemMapper.selectByPrimaryKey(newBeeMallShoppingCartItemId);
  }

  @Override
  public Boolean deleteById(Long newBeeMallShoppingCartItemId) {
    //todo userId不同不能删除
    return shoppingCartItemMapper.deleteByPrimaryKey(newBeeMallShoppingCartItemId) > 0;
  }

  @Override
  public List<ShoppingCartItemVO> getMyShoppingCartItems(Long newBeeMallUserId) {
    List<ShoppingCartItemVO> shoppingCartItemVOS = new ArrayList<>();
    List<ShoppingCartItem> shoppingCartItems = shoppingCartItemMapper.selectByUserId(newBeeMallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
    if (!CollectionUtils.isEmpty(shoppingCartItems)) {
      //查询商品信息并做数据转换
      List<Long> newBeeMallGoodsIds = shoppingCartItems.stream().map(ShoppingCartItem::getGoodsId).collect(Collectors.toList());
      List<Goods> goods = goodsMapper.selectByPrimaryKeys(newBeeMallGoodsIds);
      Map<Long, Goods> newBeeMallGoodsMap = new HashMap<>();
      if (!CollectionUtils.isEmpty(goods)) {
        newBeeMallGoodsMap = goods.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
      }
      for (ShoppingCartItem shoppingCartItem : shoppingCartItems) {
        ShoppingCartItemVO shoppingCartItemVO = new ShoppingCartItemVO();
        BeanUtil.copyProperties(shoppingCartItem, shoppingCartItemVO);
        if (newBeeMallGoodsMap.containsKey(shoppingCartItem.getGoodsId())) {
          Goods goodsTemp = newBeeMallGoodsMap.get(shoppingCartItem.getGoodsId());
          shoppingCartItemVO.setGoodsCoverImg(goodsTemp.getGoodsCoverImg());
          String goodsName = goodsTemp.getGoodsName();
          // 字符串过长导致文字超出的问题
          if (goodsName.length() > 28) {
            goodsName = goodsName.substring(0, 28) + "...";
          }
          shoppingCartItemVO.setGoodsName(goodsName);
          shoppingCartItemVO.setSellingPrice(goodsTemp.getSellingPrice());
          shoppingCartItemVOS.add(shoppingCartItemVO);
        }
      }
    }
    return shoppingCartItemVOS;
  }
}
