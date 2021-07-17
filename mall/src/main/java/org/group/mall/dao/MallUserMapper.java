package org.group.mall.dao;

import org.group.mall.entity.User;
import org.group.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallUserMapper {
  int deleteByPrimaryKey(Long userId);

  int insert(User record);

  int insertSelective(User record);

  User selectByPrimaryKey(Long userId);

  User selectByLoginName(String loginName);

  User selectByLoginNameAndPasswd(@Param("loginName") String loginName, @Param("password") String password);

  int updateByPrimaryKeySelective(User record);

  int updateByPrimaryKey(User record);

  List<User> findMallUserList(PageQueryUtil pageUtil);

  int getTotalMallUsers(PageQueryUtil pageUtil);

  int lockUserBatch(@Param("ids") Integer[] ids, @Param("lockStatus") int lockStatus);

}