package org.group.mall.service;

import org.group.mall.controller.vo.IndexConfigGoodsVO;
import org.group.mall.entity.IndexConfig;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;

import java.util.List;

public interface IndexConfigService {
  /**
   * 后台分页
   *
   * @param pageUtil
   * @return
   */
  PageResult getConfigsPage(PageQueryUtil pageUtil);

  String saveIndexConfig(IndexConfig indexConfig);

  String updateIndexConfig(IndexConfig indexConfig);

  IndexConfig getIndexConfigById(Long id);

  /**
   * 返回固定数量的首页配置商品对象(首页调用)
   *
   * @param number
   * @return
   */
  List<IndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

  Boolean deleteBatch(Long[] ids);
}
