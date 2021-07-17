package org.group.mall.dao;

import org.group.mall.entity.Goods;
import org.group.mall.entity.StockNumDTO;
import org.group.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GoodsMapper {
  int deleteByPrimaryKey(Long goodsId);

  int insert(Goods record);

  int insertSelective(Goods record);

  Goods selectByPrimaryKey(Long goodsId);

  int updateByPrimaryKeySelective(Goods record);

  int updateByPrimaryKeyWithBLOBs(Goods record);

  int updateByPrimaryKey(Goods record);

  List<Goods> findNewBeeMallGoodsList(PageQueryUtil pageUtil);

  int getTotalNewBeeMallGoods(PageQueryUtil pageUtil);

  List<Goods> selectByPrimaryKeys(List<Long> goodsIds);

  List<Goods> findNewBeeMallGoodsListBySearch(PageQueryUtil pageUtil);

  int getTotalNewBeeMallGoodsBySearch(PageQueryUtil pageUtil);

  int batchInsert(@Param("goodsList") List<Goods> goodsList);

  int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

  int batchUpdateSellStatus(@Param("orderIds") Long[] orderIds, @Param("sellStatus") int sellStatus);

}