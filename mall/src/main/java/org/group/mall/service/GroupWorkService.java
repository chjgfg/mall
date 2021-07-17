package org.group.mall.service;

import org.group.mall.controller.vo.GroupUVO;
import org.group.mall.entity.GroupWork;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;

import java.util.List;

public interface GroupWorkService {

  /**
   * 开团 添加到redis
   *
   * @param groupWorkId
   * @param userId
   * @return
   */
  GroupUVO groupWorkToRedis(Long groupWorkId, Long userId);

  /**
   * 拼团（判断人数是否完成拼团）
   *
   * @param groupWorkId
   * @param groupId
   * @param userId
   * @return
   */
  String judgRedis(Long groupWorkId, String groupId, Long userId);

  /**
   * 查看拼团
   *
   * @param id
   * @return
   */
  List<GroupUVO> findGroupWork(Long id);

//  /**
//   * 拼团失败
//   *
//   * @param groupWorkId
//   * @param groupId
//   * @param userId
//   */
//  void groupWorkFail(Long groupWorkId, String groupId, Long userId);


  /**
   * 获取拼单商品详情
   *
   * @param groupId
   * @return
   */
  GroupWork getGroupWorkById(Long groupId);

  /**
   * 拼单商品搜索
   *
   * @param pageUtil
   * @return
   */
  PageResult searchGroupWorkListBySearch(PageQueryUtil pageUtil);

  /**
   * 随机获取拼团商品
   *
   * @param number
   * @return
   */
  List<GroupWork> findSome(long number);

  String getFromRedis(String groupWorkId, String groupId, int id);

  String fromRedisToMysql(String groupWorkId, String groupId, int id);

  //时间到了
  String timeOut(Long groupWorkId, String groupId, Long userId);

  //从队伍中删除
//  String removeFromGroup(Long groupWorkId, String groupId, Long userId);

  String removeOneFromGroup(Long groupWorkId, String groupId, Long userId);

  int deleteOne(Long groupWorkId, String groupId);

  String someSeconds(Long groupWorkId, String groupId, Long userId);
}
