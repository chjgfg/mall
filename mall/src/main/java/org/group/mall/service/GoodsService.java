package org.group.mall.service;

import org.group.mall.entity.Goods;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;

import java.util.List;

public interface GoodsService {
  /**
   * 后台分页
   *
   * @param pageUtil
   * @return
   */
  PageResult getNewBeeMallGoodsPage(PageQueryUtil pageUtil);

  /**
   * 添加商品
   *
   * @param goods
   * @return
   */
  String saveNewBeeMallGoods(Goods goods);

  /**
   * 批量新增商品数据
   *
   * @param goodsList
   * @return
   */
  void batchSaveNewBeeMallGoods(List<Goods> goodsList);

  /**
   * 修改商品信息
   *
   * @param goods
   * @return
   */
  String updateNewBeeMallGoods(Goods goods);

  /**
   * 获取商品详情
   *
   * @param id
   * @return
   */
  Goods getNewBeeMallGoodsById(Long id);

  /**
   * 批量修改销售状态(上架下架)
   *
   * @param ids
   * @return
   */
  Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

  /**
   * 商品搜索
   *
   * @param pageUtil
   * @return
   */
  PageResult searchNewBeeMallGoods(PageQueryUtil pageUtil);
}
