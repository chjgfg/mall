package org.group.mall.controller.mall;

import org.group.mall.common.Constants;
import org.group.mall.common.MyException;
import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.GoodsDetailVO;
import org.group.mall.controller.vo.SearchPageCategoryVO;
import org.group.mall.entity.Goods;
import org.group.mall.service.CategoryService;
import org.group.mall.service.GoodsService;
import org.group.mall.util.BeanUtil;
import org.group.mall.util.PageQueryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class GoodsController {

  @Autowired
  private GoodsService goodsService;
  @Autowired
  private CategoryService categoryService;

    @GetMapping({"/search", "/search.html"})
//  @GetMapping({"/search"})
  public String searchPage(@RequestParam Map<String, Object> params, HttpServletRequest request) {
//    System.out.println("params = " + params);
    if (StringUtils.isEmpty(params.get("page"))) {
      params.put("page", 1);
    }
    params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
    //封装分类数据
    if (params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId") + "")) {
      Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
      SearchPageCategoryVO searchPageCategoryVO = categoryService.getCategoriesForSearch(categoryId);
      if (searchPageCategoryVO != null) {
        request.setAttribute("goodsCategoryId", categoryId);
        request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
      }
    }
    //封装参数供前端回显
    if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
      request.setAttribute("orderBy", params.get("orderBy") + "");
    }
    String keyword = "";
    //对keyword做过滤 去掉空格
    if (params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword") + "").trim())) {
      keyword = params.get("keyword") + "";
    }
//    System.out.println("keyword = " + keyword);
    request.setAttribute("keyword", keyword);
    params.put("keyword", keyword);
    //搜索上架状态下的商品
    params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
    //封装商品数据
    PageQueryUtil pageUtil = new PageQueryUtil(params);
    request.setAttribute("pageResult", goodsService.searchNewBeeMallGoods(pageUtil));
    return "mall/search";
  }

  @GetMapping("/goods/detail/{goodsId}")
  public String detailPage(@PathVariable("goodsId") Long goodsId, HttpServletRequest request) {
    if (goodsId < 1) {
      return "error/error_5xx";
    }
    Goods goods = goodsService.getNewBeeMallGoodsById(goodsId);
    if (goods == null) {
      MyException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
    }
    if (Constants.SELL_STATUS_UP != goods.getGoodsSellStatus()) {
      MyException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
    }
    GoodsDetailVO goodsDetailVO = new GoodsDetailVO();
    BeanUtil.copyProperties(goods, goodsDetailVO);
    goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
    request.setAttribute("goodsDetail", goodsDetailVO);
    return "mall/detail";
  }

}
