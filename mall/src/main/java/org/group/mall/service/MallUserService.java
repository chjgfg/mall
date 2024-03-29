package org.group.mall.service;

import org.group.mall.controller.vo.UserVO;
import org.group.mall.entity.User;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;

import javax.servlet.http.HttpSession;

public interface MallUserService {
  /**
   * 后台分页
   *
   * @param pageUtil
   * @return
   */
  PageResult getNewBeeMallUsersPage(PageQueryUtil pageUtil);

  /**
   * 用户注册
   *
   * @param loginName
   * @param password
   * @return
   */
  String register(String loginName, String password);

  /**
   * 登录
   *
   * @param loginName
   * @param passwordMD5
   * @param httpSession
   * @return
   */
  String login(String loginName, String passwordMD5, HttpSession httpSession);

  /**
   * 用户信息修改并返回最新的用户信息
   *
   * @param user
   * @return
   */
  UserVO updateUserInfo(User user, HttpSession httpSession);

  /**
   * 用户禁用与解除禁用(0-未锁定 1-已锁定)
   *
   * @param ids
   * @param lockStatus
   * @return
   */
  Boolean lockUsers(Integer[] ids, int lockStatus);

  User selectByPrimaryKey(Long userId);

  User selectByLoginName(String loginName);
}
