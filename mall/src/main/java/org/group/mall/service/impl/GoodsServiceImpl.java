package org.group.mall.service.impl;

import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.SearchGoodsVO;
import org.group.mall.dao.GoodsMapper;
import org.group.mall.entity.Goods;
import org.group.mall.service.GoodsService;
import org.group.mall.util.BeanUtil;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

  @Autowired
  private GoodsMapper goodsMapper;

  @Override
  public PageResult getNewBeeMallGoodsPage(PageQueryUtil pageUtil) {
    List<Goods> goodsList = goodsMapper.findNewBeeMallGoodsList(pageUtil);
    int total = goodsMapper.getTotalNewBeeMallGoods(pageUtil);
    PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
    return pageResult;
  }

  @Override
  public String saveNewBeeMallGoods(Goods goods) {
    if (goodsMapper.insertSelective(goods) > 0) {
      return ServiceResultEnum.SUCCESS.getResult();
    }
    return ServiceResultEnum.DB_ERROR.getResult();
  }

  @Override
  public void batchSaveNewBeeMallGoods(List<Goods> goodsList) {
    if (!CollectionUtils.isEmpty(goodsList)) {
      goodsMapper.batchInsert(goodsList);
    }
  }

  @Override
  public String updateNewBeeMallGoods(Goods goods) {
    Goods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
    if (temp == null) {
      return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }
    goods.setUpdateTime(new Date());
    if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
      return ServiceResultEnum.SUCCESS.getResult();
    }
    return ServiceResultEnum.DB_ERROR.getResult();
  }

  @Override
  public Goods getNewBeeMallGoodsById(Long id) {
    return goodsMapper.selectByPrimaryKey(id);
  }

  @Override
  public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
    return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
  }

  @Override
  public PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil) {
    List<Goods> goodsList = goodsMapper.findNewBeeMallGoodsListBySearch(pageUtil);
    int total = goodsMapper.getTotalNewBeeMallGoodsBySearch(pageUtil);
    List<SearchGoodsVO> searchGoodsVOS = new ArrayList<>();
    if (!CollectionUtils.isEmpty(goodsList)) {
      searchGoodsVOS = BeanUtil.copyList(goodsList, SearchGoodsVO.class);
      for (SearchGoodsVO searchGoodsVO : searchGoodsVOS) {
        String goodsName = searchGoodsVO.getGoodsName();
        String goodsIntro = searchGoodsVO.getGoodsIntro();
        // 字符串过长导致文字超出的问题
        if (goodsName.length() > 28) {
          goodsName = goodsName.substring(0, 28) + "...";
          searchGoodsVO.setGoodsName(goodsName);
        }
        if (goodsIntro.length() > 30) {
          goodsIntro = goodsIntro.substring(0, 30) + "...";
          searchGoodsVO.setGoodsIntro(goodsIntro);
        }
      }
    }
    PageResult pageResult = new PageResult(searchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
    return pageResult;
  }
}
