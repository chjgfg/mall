package org.group.mall.service.impl;

import org.group.mall.common.Constants;
import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.UserVO;
import org.group.mall.dao.MallUserMapper;
import org.group.mall.entity.User;
import org.group.mall.service.MallUserService;
import org.group.mall.util.BeanUtil;
import org.group.mall.util.MD5Util;
import org.group.mall.util.MallUtils;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class UserServiceImpl implements MallUserService {

  @Autowired
  private MallUserMapper mallUserMapper;

  @Override
  public PageResult getNewBeeMallUsersPage(PageQueryUtil pageUtil) {
    List<User> users = mallUserMapper.findMallUserList(pageUtil);
    int total = mallUserMapper.getTotalMallUsers(pageUtil);
    PageResult pageResult = new PageResult(users, total, pageUtil.getLimit(), pageUtil.getPage());
    return pageResult;
  }

  @Override
  public String register(String loginName, String password) {
    if (mallUserMapper.selectByLoginName(loginName) != null) {
      return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
    }
    User registerUser = new User();
    registerUser.setLoginName(loginName);
    registerUser.setNickName(loginName);
    String passwordMD5 = MD5Util.MD5Encode(password, "UTF-8");
    registerUser.setPasswordMd5(passwordMD5);
    if (mallUserMapper.insertSelective(registerUser) > 0) {
      return ServiceResultEnum.SUCCESS.getResult();
    }
    return ServiceResultEnum.DB_ERROR.getResult();
  }

  @Override
  public String login(String loginName, String passwordMD5, HttpSession httpSession) {
    User user = mallUserMapper.selectByLoginNameAndPasswd(loginName, passwordMD5);
    if (user != null && httpSession != null) {
      if (user.getLockedFlag() == 1) {
        return ServiceResultEnum.LOGIN_USER_LOCKED.getResult();
      }
      //昵称太长 影响页面展示
      if (user.getNickName() != null && user.getNickName().length() > 7) {
        String tempNickName = user.getNickName().substring(0, 7) + "..";
        user.setNickName(tempNickName);
      }
      UserVO userVO = new UserVO();
      BeanUtil.copyProperties(user, userVO);
      //设置购物车中的数量
      httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, userVO);
//      System.out.println("ServiceResultEnum.SUCCESS.getResult() = " + ServiceResultEnum.SUCCESS.getResult());
      return ServiceResultEnum.SUCCESS.getResult();
    }
//    System.out.println("ServiceResultEnum.LOGIN_ERROR.getResult() = " + ServiceResultEnum.LOGIN_ERROR.getResult());
    return ServiceResultEnum.LOGIN_ERROR.getResult();
  }

  @Override
  public UserVO updateUserInfo(User user, HttpSession httpSession) {
    UserVO userTemp = (UserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
    User userFromDB = mallUserMapper.selectByPrimaryKey(userTemp.getUserId());
    if (userFromDB != null) {
      if (!StringUtils.isEmpty(user.getNickName())) {
        userFromDB.setNickName(MallUtils.cleanString(user.getNickName()));
      }
      if (!StringUtils.isEmpty(user.getAddress())) {
        userFromDB.setAddress(MallUtils.cleanString(user.getAddress()));
      }
      if (!StringUtils.isEmpty(user.getIntroduceSign())) {
        userFromDB.setIntroduceSign(MallUtils.cleanString(user.getIntroduceSign()));
      }
      if (mallUserMapper.updateByPrimaryKeySelective(userFromDB) > 0) {
        UserVO userVO = new UserVO();
        userFromDB = mallUserMapper.selectByPrimaryKey(user.getUserId());
        BeanUtil.copyProperties(userFromDB, userVO);
        httpSession.setAttribute(Constants.MALL_USER_SESSION_KEY, userVO);
        return userVO;
      }
    }
    return null;
  }

  @Override
  public Boolean lockUsers(Integer[] ids, int lockStatus) {
    if (ids.length < 1) {
      return false;
    }
    return mallUserMapper.lockUserBatch(ids, lockStatus) > 0;
  }

  @Override
  public User selectByPrimaryKey(Long userId) {
    return mallUserMapper.selectByPrimaryKey(userId);
  }

  @Override
  public User selectByLoginName(String loginName) {
    return mallUserMapper.selectByLoginName(loginName);
  }


}
