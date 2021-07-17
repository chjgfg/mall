package org.group.mall.controller.admin;

import org.group.mall.common.Constants;
import org.group.mall.common.CategoryLevelEnum;
import org.group.mall.common.ServiceResultEnum;
import org.group.mall.entity.Goods;
import org.group.mall.entity.GoodsCategory;
import org.group.mall.service.CategoryService;
import org.group.mall.service.GoodsService;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.Result;
import org.group.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class MallGoodsController {

  @Resource
  private GoodsService goodsService;
  @Resource
  private CategoryService categoryService;

  @GetMapping("/goods")
  public String goodsPage(HttpServletRequest request) {
    request.setAttribute("path", "mall_goods");
    return "admin/mall_goods";
  }

  @GetMapping("/goods/edit")
  public String edit(HttpServletRequest request) {
    request.setAttribute("path", "edit");
    //查询所有的一级分类
    List<GoodsCategory> firstLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel());
    if (!CollectionUtils.isEmpty(firstLevelCategories)) {
      //查询一级分类列表中第一个实体的所有二级分类
      List<GoodsCategory> secondLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_TWO.getLevel());
      if (!CollectionUtils.isEmpty(secondLevelCategories)) {
        //查询二级分类列表中第一个实体的所有三级分类
        List<GoodsCategory> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_THREE.getLevel());
        request.setAttribute("firstLevelCategories", firstLevelCategories);
        request.setAttribute("secondLevelCategories", secondLevelCategories);
        request.setAttribute("thirdLevelCategories", thirdLevelCategories);
        request.setAttribute("path", "goods-edit");
        return "admin/mall_goods_edit";
      }
    }
    return "error/error_5xx";
  }

  @GetMapping("/goods/edit/{goodsId}")
  public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
    request.setAttribute("path", "edit");
    Goods newBeeMallGoods = goodsService.getNewBeeMallGoodsById(goodsId);
    if (newBeeMallGoods == null) {
      return "error/error_400";
    }
    if (newBeeMallGoods.getGoodsCategoryId() > 0) {
      if (newBeeMallGoods.getGoodsCategoryId() != null || newBeeMallGoods.getGoodsCategoryId() > 0) {
        //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
        GoodsCategory currentGoodsCategory = categoryService.getGoodsCategoryById(newBeeMallGoods.getGoodsCategoryId());
        //商品表中存储的分类id字段为三级分类的id，不为三级分类则是错误数据
        if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == CategoryLevelEnum.LEVEL_THREE.getLevel()) {
          //查询所有的一级分类
          List<GoodsCategory> firstLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel());
          //根据parentId查询当前parentId下所有的三级分类
          List<GoodsCategory> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()), CategoryLevelEnum.LEVEL_THREE.getLevel());
          //查询当前三级分类的父级二级分类
          GoodsCategory secondCategory = categoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
          if (secondCategory != null) {
            //根据parentId查询当前parentId下所有的二级分类
            List<GoodsCategory> secondLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()), CategoryLevelEnum.LEVEL_TWO.getLevel());
            //查询当前二级分类的父级一级分类
            GoodsCategory firestCategory = categoryService.getGoodsCategoryById(secondCategory.getParentId());
            if (firestCategory != null) {
              //所有分类数据都得到之后放到request对象中供前端读取
              request.setAttribute("firstLevelCategories", firstLevelCategories);
              request.setAttribute("secondLevelCategories", secondLevelCategories);
              request.setAttribute("thirdLevelCategories", thirdLevelCategories);
              request.setAttribute("firstLevelCategoryId", firestCategory.getCategoryId());
              request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
              request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
            }
          }
        }
      }
    }
    if (newBeeMallGoods.getGoodsCategoryId() == 0) {
      //查询所有的一级分类
      List<GoodsCategory> firstLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel());
      if (!CollectionUtils.isEmpty(firstLevelCategories)) {
        //查询一级分类列表中第一个实体的所有二级分类
        List<GoodsCategory> secondLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_TWO.getLevel());
        if (!CollectionUtils.isEmpty(secondLevelCategories)) {
          //查询二级分类列表中第一个实体的所有三级分类
          List<GoodsCategory> thirdLevelCategories = categoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), CategoryLevelEnum.LEVEL_THREE.getLevel());
          request.setAttribute("firstLevelCategories", firstLevelCategories);
          request.setAttribute("secondLevelCategories", secondLevelCategories);
          request.setAttribute("thirdLevelCategories", thirdLevelCategories);
        }
      }
    }
    request.setAttribute("goods", newBeeMallGoods);
    request.setAttribute("path", "goods-edit");
    return "admin/mall_goods_edit";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
  @ResponseBody
  public Result list(@RequestParam Map<String, Object> params) {
    if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    PageQueryUtil pageUtil = new PageQueryUtil(params);
    return ResultGenerator.genSuccessResult(goodsService.getNewBeeMallGoodsPage(pageUtil));
  }

  /**
   * 添加
   */
  @RequestMapping(value = "/goods/save", method = RequestMethod.POST)
  @ResponseBody
  public Result save(@RequestBody Goods newBeeMallGoods) {
    if (StringUtils.isEmpty(newBeeMallGoods.getGoodsName())
        || StringUtils.isEmpty(newBeeMallGoods.getGoodsIntro())
        || StringUtils.isEmpty(newBeeMallGoods.getTag())
        || Objects.isNull(newBeeMallGoods.getOriginalPrice())
        || Objects.isNull(newBeeMallGoods.getGoodsCategoryId())
        || Objects.isNull(newBeeMallGoods.getSellingPrice())
        || Objects.isNull(newBeeMallGoods.getStockNum())
        || Objects.isNull(newBeeMallGoods.getGoodsSellStatus())
        || StringUtils.isEmpty(newBeeMallGoods.getGoodsCoverImg())
        || StringUtils.isEmpty(newBeeMallGoods.getGoodsDetailContent())) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = goodsService.saveNewBeeMallGoods(newBeeMallGoods);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }


  /**
   * 修改
   */
  @RequestMapping(value = "/goods/update", method = RequestMethod.POST)
  @ResponseBody
  public Result update(@RequestBody Goods newBeeMallGoods) {
    if (Objects.isNull(newBeeMallGoods.getGoodsId())
        || StringUtils.isEmpty(newBeeMallGoods.getGoodsName())
        || StringUtils.isEmpty(newBeeMallGoods.getGoodsIntro())
        || StringUtils.isEmpty(newBeeMallGoods.getTag())
        || Objects.isNull(newBeeMallGoods.getOriginalPrice())
        || Objects.isNull(newBeeMallGoods.getSellingPrice())
        || Objects.isNull(newBeeMallGoods.getGoodsCategoryId())
        || Objects.isNull(newBeeMallGoods.getStockNum())
        || Objects.isNull(newBeeMallGoods.getGoodsSellStatus())
        || StringUtils.isEmpty(newBeeMallGoods.getGoodsCoverImg())
        || StringUtils.isEmpty(newBeeMallGoods.getGoodsDetailContent())) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = goodsService.updateNewBeeMallGoods(newBeeMallGoods);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }

  /**
   * 详情
   */
  @GetMapping("/goods/info/{id}")
  @ResponseBody
  public Result info(@PathVariable("id") Long id) {
    Goods goods = goodsService.getNewBeeMallGoodsById(id);
    if (goods == null) {
      return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }
    return ResultGenerator.genSuccessResult(goods);
  }

  /**
   * 批量修改销售状态
   */
  @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
  @ResponseBody
  public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
    if (ids.length < 1) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
      return ResultGenerator.genFailResult("状态异常！");
    }
    if (goodsService.batchUpdateSellStatus(ids, sellStatus)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult("修改失败");
    }
  }

}
