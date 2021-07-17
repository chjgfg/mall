package org.group.mall.service;

import org.group.mall.controller.vo.IndexCarouselVO;
import org.group.mall.entity.Carousel;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;

import java.util.List;

public interface CarouselService {
  /**
   * 后台分页
   *
   * @param pageUtil
   * @return
   */
  PageResult getCarouselPage(PageQueryUtil pageUtil);

  String saveCarousel(Carousel carousel);

  String updateCarousel(Carousel carousel);

  Carousel getCarouselById(Integer id);

  Boolean deleteBatch(Integer[] ids);

  /**
   * 返回固定数量的轮播图对象(首页调用)
   *
   * @param number
   * @return
   */
  List<IndexCarouselVO> getCarouselsForIndex(int number);
}
