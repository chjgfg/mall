package org.group.mall.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.group.mall.controller.vo.GroupUVO;
import org.group.mall.controller.vo.GroupWorkOrderUVO;
import org.group.mall.dao.GroupToRedisMapper;
import org.group.mall.dao.GroupWorkUMapper;
import org.group.mall.dao.ShoppingCartItemMapper;
import org.group.mall.entity.GroupWork;
import org.group.mall.entity.ShoppingCartItem;
import org.group.mall.service.GroupWorkService;
import org.group.mall.service.ShoppingCartService;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;
import org.group.mall.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class GroupWorkServiceImpl implements GroupWorkService {

  @Autowired
  private RedisUtil redisUtil;
  @Resource
  private GroupWorkUMapper groupWorkUMapper;
  @Resource
  private ShoppingCartItemMapper shoppingCartItemMapper;
  @Resource
  private GroupToRedisMapper groupToRedisMapper;

  @Resource
  private ShoppingCartService shoppingCartService;


  @Value("${time}")
  private int time;

  @Override
  public GroupWork getGroupWorkById(Long groupId) {
    return groupWorkUMapper.selectGroupWorkById(groupId);
  }

  @Override
  public PageResult searchGroupWorkListBySearch(PageQueryUtil pageUtil) {
    List<GroupWork> groupList = groupWorkUMapper.findGroupWorkListBySearch(pageUtil);
    int total = groupWorkUMapper.getTotalGroupWorkBySearch(pageUtil);
    return new PageResult(groupList, total, pageUtil.getLimit(), pageUtil.getPage());
  }


  @Override
  public GroupUVO groupWorkToRedis(Long groupWorkId, Long userId) {
    GroupUVO groupUVO = new GroupUVO();
    //拼团队伍id设置为UUID
    String groupId = UUID.randomUUID().toString().substring(0, 5);
    groupUVO.setGroupId(groupId);
    List<GroupWorkOrderUVO> orderUVOS = new ArrayList<>();
    GroupWorkOrderUVO orderUVO = new GroupWorkOrderUVO();
    orderUVO.setUserId(userId);
    orderUVO.setGoodsCount(1);
    orderUVO.setGroupId(groupWorkId);
    orderUVOS.add(orderUVO);
    groupUVO.setGroupWorkOrder(orderUVOS);
    GroupWork groupWork = groupWorkUMapper.selectGroupWorkById(groupWorkId);
    //拼团成功所需人数
    groupUVO.setGroupNum(groupWork.getGroup_num());
    //到期时间
    Date date = new Date(); //取时间
    Date date1 = new Date(date.getTime() + time * 60 * 1000);
    groupUVO.setEndTime(date1);
    //目前队伍拼团人数
    groupUVO.setCurrentNum(1);
    //将商品id，队伍id，用户id绑定添加到表里
    groupToRedisMapper.insertGroup(groupWorkId, groupId, userId, date1);
    //封装拼团对象,压缩成json
    String json = JSONObject.toJSONString(groupUVO);
//    redisUtil.set("groupVO:" + groupWorkId + ":" + groupId, json, (long) (60 * time), TimeUnit.SECONDS);
    redisUtil.set("groupVO:" + groupWorkId + ":" + groupId, json, (long) (1 + time), TimeUnit.MINUTES);
    return groupUVO;
  }

  /**
   * 从队伍中取消
   *
   * @param groupWorkId
   * @param groupId
   * @param userId
   * @return
   */
  @Override
  public String removeOneFromGroup(Long groupWorkId, String groupId, Long userId) {
    String fromRedis = getFromRedis(String.valueOf(groupWorkId), groupId, Math.toIntExact(userId));
//    System.out.println("fromRedis = ------------------------------------" + fromRedis);
    if (fromRedis.equals("no")) {
      return "no exits";
    } else {
      String groupVOJson = String.valueOf(redisUtil.get("groupVO:" + groupWorkId + ":" + groupId));
      GroupUVO groupVO = JSONObject.parseObject(groupVOJson, GroupUVO.class);
      int current = groupVO.getCurrentNum() - 1;
      System.out.println("current = " + current);
      if (current != 0) {
        List<GroupWorkOrderUVO> groupWorkOrder = groupVO.getGroupWorkOrder();
        for (int i = 0; i < groupWorkOrder.size(); i++) {
          if (groupWorkOrder.get(i).getUserId().equals(userId)) {
            groupWorkOrder.remove(i);
          }
        }
        groupVO.setCurrentNum(current);
        //封装拼团对象,压缩成json
        String json = JSONObject.toJSONString(groupVO);
        redisUtil.set("groupVO:" + groupWorkId + ":" + groupId, json, (long) (1 + time), TimeUnit.MINUTES);
        return "remove";
      } else {
        deleteOne(groupWorkId, groupId);
        redisUtil.remove("groupVO:" + groupWorkId + ":" + groupId);
        return "remove over";
      }
    }
  }


  /**
   * 加入队伍
   *
   * @param groupWorkId
   * @param groupId
   * @param userId
   * @return
   */
  @Override
  public String judgRedis(Long groupWorkId, String groupId, Long userId) {
    //从redis内读取字符串
    String groupVOJson = String.valueOf(redisUtil.get("groupVO:" + groupWorkId + ":" + groupId));
    if (groupVOJson.equals("")) {
      return "no exits";//队伍不存在
    }
    //解析字符串
    GroupUVO groupVO = JSONObject.parseObject(groupVOJson, GroupUVO.class);
    //判断此用户是否已加入这个队伍
    String fromResdix = getFromRedis(groupWorkId + "", groupId, Math.toIntExact(userId));
//    System.out.println("fromResdix = --------------" + fromResdix);
    if (fromResdix.equals("ok")) {
      return "exits";//已经在这个队伍中了
    } else {
      Integer current = groupVO.getCurrentNum();
      GroupWork groupWork = groupWorkUMapper.selectGroupWorkById(groupWorkId);
      //等于就不能进了
      if (groupWork.getGroup_num().equals(current)) {
        return "full";//满了
      } else {
        List<GroupWorkOrderUVO> groupWorkOrder = groupVO.getGroupWorkOrder();
        GroupWorkOrderUVO orderUVO = new GroupWorkOrderUVO();
        orderUVO.setGroupId(groupWorkId);
        orderUVO.setUserId(userId);
        orderUVO.setGoodsCount(1);
        groupWorkOrder.add(orderUVO);
        groupVO.setCurrentNum(++current);
        //封装拼团对象,压缩成json
        String json = JSONObject.toJSONString(groupVO);
        //将商品id，队伍id，用户id绑定添加到表里
        redisUtil.set("groupVO:" + groupWorkId + ":" + groupId, json, (long) (1 + time), TimeUnit.MINUTES);
        //再次看一下是否满了;
        Integer currentNum = JSONObject.parseObject(String.valueOf(redisUtil.get("groupVO:" + groupWorkId + ":" + groupId)), GroupUVO.class).getCurrentNum();
        if (groupWorkUMapper.selectGroupWorkById(groupWorkId).getGroup_num().equals(currentNum)) {
          return "ok full";
        }
        return "ok not full";
      }
    }
  }


  /**
   * 时间到了
   *
   * @param groupWorkId
   * @param groupId
   * @param userId
   * @return
   */
  @Async
  public String timeOut(Long groupWorkId, String groupId, Long userId) {

    //从redis内读取字符串
    String groupVOJson = String.valueOf(redisUtil.get("groupVO:" + groupWorkId + ":" + groupId));
    //解析字符串
    GroupUVO groupVO = JSONObject.parseObject(groupVOJson, GroupUVO.class);
    //判断此用户是否已加入这个队伍
    String fromRedis = getFromRedis(groupWorkId + "", groupId, Math.toIntExact(userId));
//    System.out.println("fromRedis = " + fromRedis);
    if (fromRedis.equals("ok")) {
      //加入后的队伍人数
      GroupWork groupWork = groupWorkUMapper.selectGroupWorkById(groupWorkId);
      Integer current = groupVO.getCurrentNum();
      //如果拼团人数没满就删了
      if (groupWork.getGroup_num().equals(current)) {//如果人够就保存
        fromRedisToMysql(groupWorkId + "", groupId, Math.toIntExact(userId));
        return "insert ok";
      }
    }
    return "no";
  }

  /**
   * 定时读
   *
   * @param groupWorkId
   * @param groupId
   * @param userId
   * @return
   */
  public String someSeconds(Long groupWorkId, String groupId, Long userId) {
    //从redis内读取字符串
    String groupVOJson = String.valueOf(redisUtil.get("groupVO:" + groupWorkId + ":" + groupId));
    //解析字符串
    GroupUVO groupVO = JSONObject.parseObject(groupVOJson, GroupUVO.class);
    GroupWork groupWork = groupWorkUMapper.selectGroupWorkById(groupWorkId);
    Integer current = groupVO.getCurrentNum();
    //如果拼团人数没满就删了
    if (groupWork.getGroup_num() > current) {
      return "not full";
    } else if (groupWork.getGroup_num().equals(current)) {//如果人够就保存
      return "good";
    } else if (groupWork.getGroup_num() < current) {//如果人够就保存
      return "so many";
    }
    return "no";
  }


  /**
   * 找到所有队伍
   *
   * @param id
   * @return
   */
  @Override
  public List<GroupUVO> findGroupWork(Long id) {
    List<String> groupIds = groupToRedisMapper.selectGroupIdByWorkId(id);
    List<GroupUVO> groupUVOS = new ArrayList<>();
    for (String groupId : groupIds) {
      //从redis内读取字符串
      String groupVOJson = String.valueOf(redisUtil.get("groupVO:" + id + ":" + groupId));
      //解析字符串
      GroupUVO groupWorkVO = JSONObject.parseObject(groupVOJson, GroupUVO.class);
      groupUVOS.add(groupWorkVO);
    }
    return groupUVOS;
  }

  /**
   * 找到一些商品
   *
   * @param number
   * @return
   */
  @Override
  public List<GroupWork> findSome(long number) {
    return groupWorkUMapper.findSome(number);
  }

  /**
   * 判断是否在Redis
   *
   * @param groupWorkId
   * @param groupId
   * @param id
   * @return
   */
  public String getFromRedis(String groupWorkId, String groupId, int id) {
    List<GroupWorkOrderUVO> groupOrderUVOS = getGroupWorkOrderUVOS(groupWorkId, groupId);
    if (groupOrderUVOS.size() != 0) {
      for (GroupWorkOrderUVO orderUVO : groupOrderUVOS) {
        Long userID = orderUVO.getUserId();
        if (userID == id) {
          return "ok";
        }
      }
    }
    return "no";
  }

  /**
   * 从Redis保存到MySQL
   *
   * @param groupWorkId
   * @param groupId
   * @param id
   * @return
   */
//  @Async
  public String fromRedisToMysql(String groupWorkId, String groupId, int id) {
    List<GroupWorkOrderUVO> groupOrderUVOS = getGroupWorkOrderUVOS(groupWorkId, groupId);
    if (groupOrderUVOS.size() == 0) {
      return "no";
    }
    for (GroupWorkOrderUVO orderUVO : groupOrderUVOS) {
      if (orderUVO.getUserId() == id) {
        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
        shoppingCartItem.setUserId(orderUVO.getUserId());
        shoppingCartItem.setGoodsId(orderUVO.getGroupId());
        shoppingCartItem.setGoodsCount(1);
        shoppingCartService.saveNewBeeMallCartItem(shoppingCartItem);
      }
    }
    return "ok";
  }

  /**
   * 从Redis和mysql删除,全部删除
   *
   * @param groupWorkId
   * @param groupId
   */
  private void deleteMysqlAndRedisAll(String groupWorkId, String groupId) {
    deleteOne(Long.parseLong(groupWorkId), groupId);
    redisUtil.remove("groupVO:" + groupWorkId + ":" + groupId);
  }

  /**
   * 从Redis和mysql删除,一个个删
   *
   * @param groupWorkId
   * @param groupId
   */
//  @Async
  void deleteMysqlAndRedisOneByOne(String groupWorkId, String groupId, Long userId) {
    try {
      System.out.println("groupWorkId = " + groupWorkId + ", groupId = " + groupId + ", userId = " + userId);
      String groupVOJson = String.valueOf(redisUtil.get("groupVO:" + groupWorkId + ":" + groupId));
      GroupUVO groupVO = JSONObject.parseObject(groupVOJson, GroupUVO.class);
      System.out.println("groupVO.getCurrentNum() = " + groupVO.getCurrentNum());
      int current = groupVO.getCurrentNum() - 1;
      System.out.println("current = " + current);
      System.out.println("groupVO.getCurrentNum() = " + groupVO.getCurrentNum());
      List<GroupWorkOrderUVO> groupWorkOrder = groupVO.getGroupWorkOrder();
      if (current != 0) {
        for (int i = 0; i < groupWorkOrder.size(); i++) {
          if (groupWorkOrder.get(i).getUserId().equals(userId)) {
            groupWorkOrder.remove(i);
            System.out.println("  current = " + current);
            groupVO.setCurrentNum(current);
            //封装拼团对象,压缩成json
            String json = JSONObject.toJSONString(groupVO);
            redisUtil.set("groupVO:" + groupWorkId + ":" + groupId, json);
          }
        }
      } else {
        for (int i = 0; i < groupWorkOrder.size(); i++) {
          if (groupWorkOrder.get(i).getUserId().equals(userId)) {
            groupWorkOrder.remove(i);
            System.out.println(" 0 current = " + current);
            groupVO.setCurrentNum(current);
            deleteOne(Long.parseLong(groupWorkId), groupId);
            redisUtil.remove("groupVO:" + groupWorkId + ":" + groupId);
          }
        }
      }
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /**
   * 获取Redis数据
   *
   * @param groupWorkId
   * @param groupId
   * @return
   */
  private List<GroupWorkOrderUVO> getGroupWorkOrderUVOS(String groupWorkId, String groupId) {
    String groupVOJson = String.valueOf(redisUtil.get("groupVO:" + groupWorkId + ":" + groupId));
    GroupUVO groupJson = JSONObject.parseObject(groupVOJson, GroupUVO.class);
    if (groupJson == null) {
      return new ArrayList<GroupWorkOrderUVO>();
    }
    List<GroupWorkOrderUVO> groupWorkOrder = groupJson.getGroupWorkOrder();
    if (!StringUtils.isEmpty(groupWorkOrder)) {
      return groupJson.getGroupWorkOrder();
    }
    groupWorkOrder.add(new GroupWorkOrderUVO());
    return groupWorkOrder;
  }

  /**
   * 删除MySQL的数据
   *
   * @param groupWorkId
   * @param groupId
   * @return
   */
  @Override
  public int deleteOne(Long groupWorkId, String groupId) {
    return groupToRedisMapper.deleteOne(groupWorkId, groupId);
  }


}