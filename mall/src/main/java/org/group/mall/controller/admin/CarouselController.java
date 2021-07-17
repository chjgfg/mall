package org.group.mall.controller.admin;

import org.group.mall.common.ServiceResultEnum;
import org.group.mall.entity.Carousel;
import org.group.mall.service.CarouselService;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.Result;
import org.group.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
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
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class CarouselController {

  @Resource
  CarouselService carouselService;

  @GetMapping("/carousels")
  public String carouselPage(HttpServletRequest request) {
    request.setAttribute("path", "mall_carousel");
    return "admin/mall_carousel";
  }

  /**
   * 列表
   */
  @RequestMapping(value = "/carousels/list", method = RequestMethod.GET)
  @ResponseBody
  public Result list(@RequestParam Map<String, Object> params) {
    if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    PageQueryUtil pageUtil = new PageQueryUtil(params);
    return ResultGenerator.genSuccessResult(carouselService.getCarouselPage(pageUtil));
  }

  /**
   * 添加
   */
  @RequestMapping(value = "/carousels/save", method = RequestMethod.POST)
  @ResponseBody
  public Result save(@RequestBody Carousel carousel) {
    if (StringUtils.isEmpty(carousel.getCarouselUrl())
        || Objects.isNull(carousel.getCarouselRank())) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = carouselService.saveCarousel(carousel);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }


  /**
   * 修改
   */
  @RequestMapping(value = "/carousels/update", method = RequestMethod.POST)
  @ResponseBody
  public Result update(@RequestBody Carousel carousel) {
    if (Objects.isNull(carousel.getCarouselId())
        || StringUtils.isEmpty(carousel.getCarouselUrl())
        || Objects.isNull(carousel.getCarouselRank())) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = carouselService.updateCarousel(carousel);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }

  /**
   * 详情
   */
  @GetMapping("/carousels/info/{id}")
  @ResponseBody
  public Result info(@PathVariable("id") Integer id) {
    Carousel carousel = carouselService.getCarouselById(id);
    if (carousel == null) {
      return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }
    return ResultGenerator.genSuccessResult(carousel);
  }

  /**
   * 删除
   */
  @RequestMapping(value = "/carousels/delete", method = RequestMethod.POST)
  @ResponseBody
  public Result delete(@RequestBody Integer[] ids) {
    if (ids.length < 1) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    if (carouselService.deleteBatch(ids)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult("删除失败");
    }
  }

}
