package org.group.mall.controller.mall;

import org.group.mall.common.Constants;
import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.UserVO;
import org.group.mall.entity.User;
import org.group.mall.service.MallUserService;
import org.group.mall.util.MD5Util;
import org.group.mall.util.Result;
import org.group.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PersonalController {

  @Resource
  private MallUserService mallUserService;

  @GetMapping("/personal")
  public String personalPage(HttpServletRequest request,
                             HttpSession httpSession) {
    request.setAttribute("path", "personal");
    return "mall/personal";
  }

  @GetMapping("/logout")
  public String logout(HttpSession httpSession) {
    httpSession.removeAttribute(Constants.MALL_USER_SESSION_KEY);
    return "mall/login";
  }

  @GetMapping({"/login", "login.html"})
//  @GetMapping({"/login"})
  public String loginPage() {
    return "mall/login";
  }

  @GetMapping({"/register", "register.html"})
//  @GetMapping({"/register"})
  public String registerPage() {
    return "mall/register";
  }

  @GetMapping("/personal/addresses")
  public String addressesPage() {
    return "mall/addresses";
  }

  @PostMapping("/login")
  @ResponseBody
  public Result login(@RequestParam("loginName") String loginName,
                      @RequestParam("verifyCode") String verifyCode,
                      @RequestParam("password") String password,
                      HttpSession httpSession) {
    if (StringUtils.isEmpty(loginName)) {
      return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
    }
    if (StringUtils.isEmpty(password)) {
      return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
    }
    if (StringUtils.isEmpty(verifyCode)) {
      return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
    }
    String kaptchaCode = httpSession.getAttribute(Constants.MALL_VERIFY_CODE_KEY) + "";

    if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.toLowerCase().equals(kaptchaCode)) {
      return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
    }
    //todo 清verifyCode
    String loginResult = mallUserService.login(loginName, MD5Util.MD5Encode(password, "UTF-8"), httpSession);
    User user = mallUserService.selectByLoginName(loginName);
    httpSession.setAttribute("user", user);
    //登录成功
//    System.out.println("loginResult = " + loginResult);
    if (ServiceResultEnum.SUCCESS.getResult().equals(loginResult)) {
      return ResultGenerator.genSuccessResult();
    }
    //登录失败
//    System.out.println("ResultGenerator.genFailResult(loginResult) = " + ResultGenerator.genFailResult(loginResult));
    return ResultGenerator.genFailResult(loginResult);
  }

  @PostMapping("/register")
  @ResponseBody
  public Result register(@RequestParam("loginName") String loginName,
                         @RequestParam("verifyCode") String verifyCode,
                         @RequestParam("password") String password,
                         HttpSession httpSession) {
    if (StringUtils.isEmpty(loginName)) {
      return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_NULL.getResult());
    }
    if (StringUtils.isEmpty(password)) {
      return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_PASSWORD_NULL.getResult());
    }
    if (StringUtils.isEmpty(verifyCode)) {
      return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_NULL.getResult());
    }
    String kaptchaCode = httpSession.getAttribute(Constants.MALL_VERIFY_CODE_KEY) + "";
    if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.toLowerCase().equals(kaptchaCode)) {
      return ResultGenerator.genFailResult(ServiceResultEnum.LOGIN_VERIFY_CODE_ERROR.getResult());
    }
    //todo 清verifyCode
    String registerResult = mallUserService.register(loginName, password);
    //注册成功
    if (ServiceResultEnum.SUCCESS.getResult().equals(registerResult)) {
      return ResultGenerator.genSuccessResult();
    }
    //注册失败
    return ResultGenerator.genFailResult(registerResult);
  }

  @PostMapping("/personal/updateInfo")
  @ResponseBody
  public Result updateInfo(@RequestBody User user, HttpSession httpSession) {
    UserVO mallUserTemp = mallUserService.updateUserInfo(user, httpSession);
    if (mallUserTemp == null) {
      return ResultGenerator.genFailResult("修改失败");
    } else {
      //返回成功
      return ResultGenerator.genSuccessResult();
    }
  }

  @PostMapping("/selectByPrimaryKey/{userId}")
  @ResponseBody
  public User selectByPrimaryKey(@PathVariable long userId) {
    return mallUserService.selectByPrimaryKey(userId);
  }

}
