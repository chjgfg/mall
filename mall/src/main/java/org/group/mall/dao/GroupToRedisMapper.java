package org.group.mall.dao;

import org.group.mall.entity.GroupToRedis;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

public interface GroupToRedisMapper {

  @Insert("insert into group_to_redis(group_work_id,group_id,user_id,endTime) values(#{groupWorkId},#{groupId},#{userId},#{date})")
  void insertGroup(Long groupWorkId, String groupId, Long userId, Date date);

  @Select("select group_id from group_to_redis where group_work_id=#{GroupWorkId}")
  List<String> selectGroupIdByWorkId(Long GroupWorkId);

  @Select("select group_id from group_to_redis where group_work_id=#{GroupWorkId} and user_id=#{userId}")
  String selectGroupIdByWorkIdAndUserId(Long GroupWorkId, Long userId);

  @Select("select * from group_to_redis where group_work_id=#{GroupWorkId} and user_id=#{userId}")
  List<GroupToRedis> selectByGroupWorkIdAndUserId(Long GroupWorkId, Long userId);

  @Delete("delete from group_to_redis where group_id = #{groupId}")
  void deleteBygroupId(String groupId);

  @Delete("delete from group_to_redis where group_id = #{groupId} and user_id =#{userId}")
  int deleteBygroupIdAndUserId(String groupId, Long userId);

  @Select("select * from group_to_redis where group_work_id=#{GroupWorkId} and group_id=#{groupId} and user_id=#{userId}")
  GroupToRedis selectOne(Long groupWorkId, String groupId, Long userId);

  @Delete("delete from group_to_redis where group_work_id=#{groupWorkId} and group_id=#{groupId}")
  int deleteOne(Long groupWorkId, String groupId);

}
