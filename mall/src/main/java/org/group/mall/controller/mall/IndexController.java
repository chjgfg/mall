package org.group.mall.controller.mall;

import org.group.mall.common.Constants;
import org.group.mall.common.IndexConfigTypeEnum;
import org.group.mall.controller.vo.IndexCarouselVO;
import org.group.mall.controller.vo.IndexCategoryVO;
import org.group.mall.controller.vo.IndexConfigGoodsVO;
import org.group.mall.service.CarouselService;
import org.group.mall.service.CategoryService;
import org.group.mall.service.IndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

  @Resource
  private CarouselService carouselService;

  @Resource
  private IndexConfigService indexConfigService;

  @Resource
  private CategoryService categoryService;

  @GetMapping({"/index", "/", "/index.html"})
//  @GetMapping({"/index", "/"})
  public String indexPage(HttpServletRequest request) {
    List<IndexCategoryVO> categories = categoryService.getCategoriesForIndex();
    if (CollectionUtils.isEmpty(categories)) {
      return "error/error_5xx";
    }
    List<IndexCarouselVO> carousels = carouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
    List<IndexConfigGoodsVO> hotGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
    List<IndexConfigGoodsVO> newGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
    List<IndexConfigGoodsVO> recommendGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
    request.setAttribute("categories", categories);//分类数据
    request.setAttribute("carousels", carousels);//轮播图
    request.setAttribute("hotGoodses", hotGoodses);//热销商品
    request.setAttribute("newGoodses", newGoodses);//新品
    request.setAttribute("recommendGoodses", recommendGoodses);//推荐商品
    return "mall/index";
  }
}
