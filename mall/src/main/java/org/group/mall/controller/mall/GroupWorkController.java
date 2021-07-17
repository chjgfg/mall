package org.group.mall.controller.mall;

import org.group.mall.common.Constants;
import org.group.mall.common.MyException;
import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.GroupUVO;
import org.group.mall.controller.vo.UserVO;
import org.group.mall.entity.GroupWork;
import org.group.mall.service.GroupWorkService;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;
import org.group.mall.util.Result;
import org.group.mall.util.ResultGenerator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

@Controller
public class GroupWorkController {

  @Resource
  private GroupWorkService groupWorkServiceService;

  private Queue<ArrayList<String>> queue = new LinkedList<>();

  /**
   * 页面跳转
   *
   * @return
   */
  @GetMapping("/groups")
  public String toNotice() {
    return "mall/groups";
  }


  /**
   * 页面跳转
   *
   * @param request
   * @param response
   * @return
   * @throws IOException
   */
  @GetMapping("/group-detail")
  public String toGroupDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (null == request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY)) {
      response.sendRedirect(request.getContextPath() + "/login");
      return "mall/login";
    } else {
//      response.sendRedirect(request.getContextPath() + "/group-detail");
      return "mall/group-detail";
    }
  }


  /**
   * 搜索拼单商品
   *
   * @param params
   * @param request
   * @return
   */
  @GetMapping({"/searchGroup"})
  @ResponseBody
  public PageResult searchPage(@RequestParam Map<String, Object> params, HttpServletRequest request) {
    if (StringUtils.isEmpty(params.get("page"))) {
      params.put("page", 1);
    }
    params.put("limit", 10);
    //封装参数供前端回显
    if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
      request.setAttribute("orderBy", params.get("orderBy") + "");
    }
    String keyword = "";
    //对keyword做过滤 去掉空格
    if (params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword") + "").trim())) {
      keyword = params.get("keyword") + "";
    }
    request.setAttribute("keyword", keyword);
    params.put("keyword", keyword);
    //搜索上架状态下的商品
    params.put("goodsSellStatus", Constants.SELL_STATUS_UP);
    //封装商品数据
    PageQueryUtil pageUtil = new PageQueryUtil(params);
    return groupWorkServiceService.searchGroupWorkListBySearch(pageUtil);
  }

  /**
   * 显示商品详情
   *
   * @param groupId
   * @param request
   * @return
   */
  @GetMapping("/groupWork/detail/{groupId}")
  public String detailPage(@PathVariable("groupId") Long groupId, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    if (groupId < 1) {
      return "error/error_5xx";
    }
    if (null != request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY)) {
      GroupWork goods = groupWorkServiceService.getGroupWorkById(groupId);
      if (goods == null) {
        MyException.fail(ServiceResultEnum.GOODS_NOT_EXIST.getResult());
      }
      if (Constants.SELL_STATUS_UP != goods.getGroup_sell_status()) {
        MyException.fail(ServiceResultEnum.GOODS_PUT_DOWN.getResult());
      }
      request.setAttribute("goods", goods);
      return "mall//group-detail";
    }
    response.sendRedirect(request.getContextPath() + "/login");
    return "mall/index";
  }

  /**
   * 我就是队长！ 开团
   *
   * @param groupWorkId
   * @param httpSession
   * @return
   */
  @PostMapping("/saveToRedis/{groupWorkId}")
  @ResponseBody
  public Result saveToRedis(@PathVariable Long groupWorkId, HttpSession httpSession) {
    UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
    GroupUVO saveToRedis = groupWorkServiceService.groupWorkToRedis(groupWorkId, user.getUserId());
    //添加成功
//    System.out.println("groupWorkId = " + groupWorkId);
    return ResultGenerator.genSuccessResult(saveToRedis);
  }

  /**
   * 加入拼团队伍
   *
   * @param groupWorkId
   * @param groupId
   * @param httpSession
   * @return
   */
  @PostMapping("/joinGroup/{groupWorkId}/{groupId}")
  @ResponseBody
  public String joinGroup(@PathVariable Long groupWorkId, @PathVariable String groupId, HttpSession httpSession) {
    UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
    return groupWorkServiceService.judgRedis(groupWorkId, groupId, user.getUserId());
  }

  /**
   * 时间到拼团人数判断
   *
   * @param groupWorkId
   * @param groupId
   * @param httpSession
   * @return
   */
  @PostMapping("/groupWorkFail/{groupWorkId}/{groupId}")
  @ResponseBody
  @Async
  public synchronized String groupWorkFail(@PathVariable Long groupWorkId, @PathVariable String groupId, HttpSession httpSession) {
    try {
      UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
//      ArrayList<String> list = new ArrayList<>();
//      list.add(String.valueOf(groupWorkId));
//      list.add(groupId);
//      list.add(user.getUserId() + "");
//      queue.offer(list);
//      String s = null;
//      for (ArrayList<String> q : queue) {
//        System.out.println("q[0] = " + q.get(0) + ", q[1] = " + q.get(1) + ", q[2] = " + q.get(2));
//        s = groupWorkServiceService.timeOut(Long.parseLong(q.get(0)), q.get(1), Long.parseLong(q.get(2)));
//        queue.poll();
//      }
//      queue.remove();
//      for (ArrayList<String> q : queue) {
//        System.out.println("q[0] = " + q.get(0) + ", q[1] = " + q.get(1) + ", q[2] = " + q.get(2));
////        s = groupWorkServiceService.timeOut(Long.parseLong(q.get(0)), q.get(1), Long.parseLong(q.get(2)));
//      }
//      return s;
      return groupWorkServiceService.timeOut(groupWorkId, groupId, user.getUserId());
    } catch (Exception e) {
      System.out.println(e);
    }
    return "";
  }

  /**
   * 定时读
   *
   * @param groupWorkId
   * @param groupId
   * @param httpSession
   * @return
   */
  @PostMapping("/someSeconds/{groupWorkId}/{groupId}")
  @ResponseBody
  public String someSeconds(@PathVariable Long groupWorkId, @PathVariable String groupId, HttpSession httpSession) {
    if (groupId.equals("") || groupWorkId.equals("")) {
      return "no";
    }
    UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
    return groupWorkServiceService.someSeconds(groupWorkId, groupId, user.getUserId());
  }

  /**
   * 显示一个商品所有的队伍
   *
   * @param groupWorkId
   * @return
   */
  @PostMapping("/findGroup/{groupWorkId}")
  @ResponseBody
  public Result findGroup(@PathVariable Long groupWorkId) {
    List<GroupUVO> groups = groupWorkServiceService.findGroupWork(groupWorkId);
    return ResultGenerator.genSuccessResult(groups);
  }


  /**
   * 找到随机的几个商品
   *
   * @param number
   * @return
   */
  @GetMapping({"/findSome/{number}"})
  @ResponseBody
  public List<GroupWork> findSome(@PathVariable String number) {
    return groupWorkServiceService.findSome(Long.parseLong(number));
  }


  /**
   * 从redis查到自己的拼单信息
   *
   * @param groupWorkId
   * @param groupId
   * @param id
   * @return
   */
  @GetMapping({"/getFromResdix/{groupWorkId}/{groupId}/{id}"})
  @ResponseBody
  public String getFromResdix(@PathVariable String groupWorkId, @PathVariable String groupId, @PathVariable int id) {
//    System.out.println(groupWorkId + " --- " + groupId + " --- " + id);
    return groupWorkServiceService.getFromRedis(groupWorkId, groupId, id);
  }


  /**
   * 从队伍中删除
   *
   * @param groupWorkId
   * @param groupId
   * @return
   */
  @PostMapping({"/removeFromGroup/{groupWorkId}/{groupId}"})
  @ResponseBody
  public String removeFromGroup(@PathVariable String groupWorkId, @PathVariable String groupId, HttpSession httpSession) {
    try {
      UserVO user = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
      return groupWorkServiceService.removeOneFromGroup(Long.parseLong(groupWorkId), groupId, user.getUserId());
    } catch (NumberFormatException e) {
      e.getStackTrace();
    }
    return "no";
  }


}