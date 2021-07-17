package org.group.mall.controller.mall;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class MallNoticeController {

  @Autowired
  private NoticeService noticeUService;

  /**
   * 页面跳转
   *
   * @return
   */
  @GetMapping("/notice")
  public String getAnnoncement() {
    return "mall/notice";
  }

  /**
   * 通过id 请求
   *
   * @param goodsId
   * @param request
   * @return
   */
  @GetMapping("/notice/{goodsId}")
  public String notice(@PathVariable int goodsId, HttpServletRequest request) throws ParseException {
    Notice notice = noticeUService.selectNoticeById((long) goodsId);
    request.setAttribute("notice", notice);
    return "mall/notice-detail";
  }


  /**
   * 分页显示所有
   *
   * @param page
   * @param limit
   * @return
   */
  @PostMapping("/mall/notice")
  @ResponseBody
  public Result noticeListPage(@RequestParam(defaultValue = "1") Long page, @RequestParam(defaultValue = "5") Long limit) {
    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isEmpty(params.get("page"))) {
      params.put("page", page);
    }
    params.put("limit", limit);
    //封装公告数据
    PageQueryUtil pageUtil = new PageQueryUtil(params);
    return ResultGenerator.genSuccessResult(noticeUService.findNoticeList(pageUtil));
  }

}

