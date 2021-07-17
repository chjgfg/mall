package org.group.mall.service;

import org.group.mall.controller.vo.IndexCategoryVO;
import org.group.mall.controller.vo.SearchPageCategoryVO;
import org.group.mall.entity.GoodsCategory;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;

import java.util.List;

public interface CategoryService {
  /**
   * 后台分页
   *
   * @param pageUtil
   * @return
   */
  PageResult getCategorisPage(PageQueryUtil pageUtil);

  String saveCategory(GoodsCategory goodsCategory);

  String updateGoodsCategory(GoodsCategory goodsCategory);

  GoodsCategory getGoodsCategoryById(Long id);

  Boolean deleteBatch(Integer[] ids);

  /**
   * 返回分类数据(首页调用)
   *
   * @return
   */
  List<IndexCategoryVO> getCategoriesForIndex();

  /**
   * 返回分类数据(搜索页调用)
   *
   * @param categoryId
   * @return
   */
  SearchPageCategoryVO getCategoriesForSearch(Long categoryId);

  /**
   * 根据parentId和level获取分类列表
   *
   * @param parentIds
   * @param categoryLevel
   * @return
   */
  List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);
}
