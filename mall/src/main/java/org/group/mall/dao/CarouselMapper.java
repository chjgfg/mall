package org.group.mall.dao;

import org.group.mall.entity.Carousel;
import org.group.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CarouselMapper {
  int deleteByPrimaryKey(Integer carouselId);

  int insert(Carousel record);

  int insertSelective(Carousel record);

  Carousel selectByPrimaryKey(Integer carouselId);

  int updateByPrimaryKeySelective(Carousel record);

  int updateByPrimaryKey(Carousel record);

  List<Carousel> findCarouselList(PageQueryUtil pageUtil);

  int getTotalCarousels(PageQueryUtil pageUtil);

  int deleteBatch(Integer[] ids);

  List<Carousel> findCarouselsByNum(@Param("number") int number);

//  @Insert("insert into record values ()")
//  int insertOne(Carousel record);

  /*
    @Insert("insert into users(name,age) values(#{name},#{age})")
    public void insertT(User user);

    @Delete("delete from users where id=#{id}")
    public void deleteById(int id);

    @Update("update users set name=#{name},age=#{age} where id=#{id}")
    public void updateT(User user);

    @Select("select * from users where id=#{id}")
    public User getUser(int id);

    @Select("select * from users")
    public List<User> getAllUsers();


  */
}