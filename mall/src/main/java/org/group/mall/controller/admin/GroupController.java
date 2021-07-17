package org.group.mall.controller.admin;

import org.group.mall.common.Constants;
import org.group.mall.common.CategoryLevelEnum;
import org.group.mall.common.ServiceResultEnum;
import org.group.mall.entity.GoodsCategory;
import org.group.mall.entity.MallGroup;
import org.group.mall.service.CategoryService;
import org.group.mall.service.GroupService;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.Result;
import org.group.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GroupController {


  @Autowired
  private GroupService groupService;

  @Resource
  private CategoryService categoryService;

  /**
   * 跳转拼团页面
   */
  @GetMapping("/group")
  public String goodsPage(HttpServletRequest request) {
    request.setAttribute("path", "mall_group");
    return "admin/mall_group";
  }


  @GetMapping("/group/edit")
  public String edit(HttpServletRequest request) {
    request.setAttribute("path", "group-edit");
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
        request.setAttribute("path", "group-edit");
        return "admin/mall_group_edit";
      }
    }
    return "error/error_5xx";
  }

  /**
   * 分页查询 拼单数据
   *
   * @param params
   * @return
   */
  @RequestMapping(value = "/group/list", method = RequestMethod.GET)
  @ResponseBody
  public Result list(@RequestParam Map<String, Object> params) {
    if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    PageQueryUtil pageUtil = new PageQueryUtil(params);
    return ResultGenerator.genSuccessResult(groupService.getNewBeeMallGroupPage(pageUtil));
  }

  /**
   * 添加新商品到拼单
   */
  @RequestMapping(value = "/group/save", method = RequestMethod.POST)
  @ResponseBody
  public Result save(@RequestBody MallGroup mallGroup) {
    if (StringUtils.isEmpty(mallGroup.getGroupName())
        || StringUtils.isEmpty(mallGroup.getGroupIntro())
        || Objects.isNull(mallGroup.getGroupCategoryId())
        || StringUtils.isEmpty(mallGroup.getGroupCoverImg())
        || StringUtils.isEmpty(mallGroup.getGroupDetailContent())
        || Objects.isNull(mallGroup.getOriginalPrice())
        || Objects.isNull(mallGroup.getStockNum())
        || StringUtils.isEmpty(mallGroup.getTag())
        || Objects.isNull(mallGroup.getGroupNum())
        || Objects.isNull(mallGroup.getGroupPrice())
        || Objects.isNull(mallGroup.getGroupSellStatus())) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = groupService.saveNewBeeMallGroup(mallGroup);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
//      System.out.println("ResultGenerator.genSuccessResult() = " + ResultGenerator.genSuccessResult());
      return ResultGenerator.genSuccessResult();
    } else {
//      System.out.println("ResultGenerator.genFailResult(result) = " + ResultGenerator.genFailResult(result));
      return ResultGenerator.genFailResult(result);
    }
  }


  /**
   * 添加已有商品到
   */
  @GetMapping(value = "/group/selectGood/{id}")
  public String saveExist(HttpServletRequest request, @PathVariable("id") Long id) {
    MallGroup group = groupService.saveNewBeeMallGroupExist(id);
    if (Objects.isNull(group)) {
      return "error/error_400";
    }

    if (group.getGroupCategoryId() > 0) {
      if (group.getGroupCategoryId() != null || group.getGroupCategoryId() > 0) {
        //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
        GoodsCategory currentGoodsCategory = categoryService.getGoodsCategoryById(group.getGroupCategoryId());
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

    if (group.getGroupCategoryId() == 0) {
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
    request.setAttribute("group", group);
    request.setAttribute("path", "group-edit");
    return "admin/mall_group_edit";
  }


  @GetMapping("/group/edit/{goodsId}")
  public String select(HttpServletRequest request, @PathVariable("goodsId") Long id) {
    MallGroup group = groupService.selectNewBeeMallGroup(id);

    if (Objects.isNull(group)) {
      return "error/error_400";
    }

    if (group.getGroupCategoryId() > 0) {
      if (group.getGroupCategoryId() != null || group.getGroupCategoryId() > 0) {
        //有分类字段则查询相关分类数据返回给前端以供分类的三级联动显示
        GoodsCategory currentGoodsCategory = categoryService.getGoodsCategoryById(group.getGroupCategoryId());
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

    if (group.getGroupCategoryId() == 0) {
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
    request.setAttribute("group", group);
    request.setAttribute("path", "group-edit");
    return "admin/mall_group_edit";
  }


  @RequestMapping(value = "/group/update", method = RequestMethod.POST)
  @ResponseBody
  public Result update(@RequestBody MallGroup mallGroup) {
    if (StringUtils.isEmpty(mallGroup.getGroupName())
        || StringUtils.isEmpty(mallGroup.getGroupIntro())
        || Objects.isNull(mallGroup.getGroupCategoryId())
        || StringUtils.isEmpty(mallGroup.getGroupCoverImg())
        || StringUtils.isEmpty(mallGroup.getGroupDetailContent())
        || Objects.isNull(mallGroup.getOriginalPrice())
        || Objects.isNull(mallGroup.getStockNum())
        || StringUtils.isEmpty(mallGroup.getTag())
        || Objects.isNull(mallGroup.getGroupNum())
        || Objects.isNull(mallGroup.getGroupPrice())
        || Objects.isNull(mallGroup.getGroupSellStatus())) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = groupService.updateNewBeeMallGroup(mallGroup);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }

  /**
   * 批量修改销售状态
   */
  @RequestMapping(value = "/group/status/{sellStatus}", method = RequestMethod.PUT)
  @ResponseBody
  public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
    if (ids.length < 1) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
      return ResultGenerator.genFailResult("状态异常！");
    }
    if (groupService.batchUpdateSellStatus(ids, sellStatus)) {
      return ResultGenerator.genSuccessResult();
    }
    return ResultGenerator.genFailResult("修改失败");
  }


  /**
   * 判断是否已经是拼团商品
   */
  @GetMapping(value = "/group/exist/{id}")
  @ResponseBody
  public Result exist(@PathVariable("id") Long id) {
//    System.out.println("+++++" + id);
    Boolean bool = groupService.selectExist(id);

    if (bool) {
      return ResultGenerator.genFailResult("该商品已经是拼团商品");
    } else {
      return ResultGenerator.genSuccessResult();
    }
  }

}





