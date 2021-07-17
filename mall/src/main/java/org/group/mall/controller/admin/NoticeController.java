package org.group.mall.controller.admin;

import org.group.mall.common.Constants;
import org.group.mall.common.ServiceResultEnum;
import org.group.mall.entity.Notice;
import org.group.mall.service.NoticeService;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.Result;
import org.group.mall.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class NoticeController {

  @Autowired
  private NoticeService noticeService;

  /**
   * 跳转拼团页面
   */
  @GetMapping("/notice")
  public String noticePage(HttpServletRequest request) {
    request.setAttribute("path", "mall_notice");
    return "admin/mall_notice";
  }

  /**
   * 跳转拼团页面
   */
  @GetMapping("/notice/edit")
  public String noticeEditPage(HttpServletRequest request) {
    request.setAttribute("path", "mall_notice_edit");
    return "admin/mall_notice_edit";
  }

  /**
   * 分页查询 拼单数据
   *
   * @param params
   * @return
   */
  @RequestMapping(value = "/notice/list", method = RequestMethod.GET)
  @ResponseBody
  public Result list(@RequestParam Map<String, Object> params) {
    if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    PageQueryUtil pageUtil = new PageQueryUtil(params);
    return ResultGenerator.genSuccessResult(noticeService.getNewBeeMallNoticePage(pageUtil));
  }


  /**
   * 批量修改销售状态
   */
  @RequestMapping(value = "/notice/status/{status}", method = RequestMethod.PUT)
  @ResponseBody
  public Result delete(@RequestBody Long[] ids, @PathVariable("status") int status) {
    if (ids.length < 1) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    if (status != Constants.SELL_STATUS_UP && status != Constants.SELL_STATUS_DOWN) {
      return ResultGenerator.genFailResult("状态异常！");
    }
    if (noticeService.batchUpdateSellStatus(ids, status)) {
      return ResultGenerator.genSuccessResult();
    }
    return ResultGenerator.genFailResult("修改失败");
  }

  @RequestMapping(value = "/notice/update", method = RequestMethod.POST)
  @ResponseBody
  public Result update(@RequestBody Notice notice) {
    if (StringUtils.isEmpty(notice.getContext())
        || Objects.isNull(notice.getStatus())
        || Objects.isNull(notice.getNoticeId())
        || StringUtils.isEmpty(notice.getTitle())
    ) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = noticeService.updateNewBeeMallNotice(notice);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }

  @GetMapping("/notice/edit/{noticeId}")
  public String select(HttpServletRequest request, @PathVariable("noticeId") Long id) {
    if (Objects.isNull(id)) {
      return "error/error_400";
    }
    Notice newBeeMallNotice = noticeService.selectNewBeeMallNotice(id);
    request.setAttribute("notice", newBeeMallNotice);
    request.setAttribute("path", "notice-edit");
    return "admin/mall_notice_edit";
  }

  /**
   * 添加新公告
   */
  @RequestMapping(value = "/notice/save", method = RequestMethod.POST)
  @ResponseBody
  public Result save(@RequestBody Notice notice) {
    if (StringUtils.isEmpty(notice.getContext())
        || Objects.isNull(notice.getStatus())
        || StringUtils.isEmpty(notice.getTitle())) {
      return ResultGenerator.genFailResult("参数异常！");
    }
    String result = noticeService.saveNewBeeMallNotice(notice);
    if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
      return ResultGenerator.genSuccessResult();
    } else {
      return ResultGenerator.genFailResult(result);
    }
  }
}
