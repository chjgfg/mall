package org.group.mall.service.impl;

import org.group.mall.common.ServiceResultEnum;
import org.group.mall.dao.GoodsMapper;
import org.group.mall.dao.GroupMapper;
import org.group.mall.entity.Goods;
import org.group.mall.entity.MallGroup;
import org.group.mall.service.GroupService;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class GroupServiceImpl implements GroupService {

  @Autowired
  private GroupMapper groupMapper;

  @Autowired
  private GoodsMapper goodsMapper;

  /**
   * 后台分页
   *
   * @param pageUtil
   * @return
   */
  @Override
  public PageResult getNewBeeMallGroupPage(PageQueryUtil pageUtil) {
    List<MallGroup> groupList = groupMapper.findNewBeeMallGRoupList(pageUtil);
    int total = groupMapper.getTotalNewBeeMallGroup(pageUtil);
    PageResult pageResult = new PageResult(groupList, total, pageUtil.getLimit(), pageUtil.getPage());
    return pageResult;
  }

  /**
   * 添加商品
   *
   * @param mallGroup
   * @return
   */
  @Override
  public String saveNewBeeMallGroup(MallGroup mallGroup) {

    Goods goods = new Goods();
    goods.setGoodsName(mallGroup.getGroupName());
    goods.setGoodsIntro(mallGroup.getGroupIntro());
    goods.setGoodsCategoryId(mallGroup.getGroupCategoryId());
    goods.setGoodsCoverImg(mallGroup.getGroupCoverImg());
    goods.setGoodsCarousel(mallGroup.getGroupCoverImg());
    goods.setOriginalPrice(mallGroup.getOriginalPrice());
    goods.setSellingPrice(mallGroup.getOriginalPrice());
    goods.setStockNum(mallGroup.getStockNum());
    goods.setTag(mallGroup.getTag());
    int a = mallGroup.getGroupSellStatus();
    byte b = (byte) a;
    goods.setGoodsSellStatus(b);
    goods.setGoodsDetailContent(mallGroup.getGroupDetailContent());
    goods.setCreateTime(new Date());
    goods.setUpdateTime(new Date());
    goods.setCreateUser(0);
    goods.setUpdateUser(0);
    int i = goodsMapper.insertSelective(goods);
//    System.out.println("--------" + goods.getGoodsId());
    mallGroup.setGroupId(goods.getGoodsId());
    if (groupMapper.insertSelective(mallGroup) > 0) {
//      System.out.println("ServiceResultEnum.SUCCESS.getResult() = " + ServiceResultEnum.SUCCESS.getResult());
      return ServiceResultEnum.SUCCESS.getResult();
    }
//    System.out.println("ServiceResultEnum.DB_ERROR.getResult() = " + ServiceResultEnum.DB_ERROR.getResult());
    return ServiceResultEnum.DB_ERROR.getResult();

  }

  /**
   * 把已有商品封装成拼团商品
   *
   * @param id
   * @return
   */
  @Override
  public MallGroup saveNewBeeMallGroupExist(Long id) {

    Goods goods = goodsMapper.selectByPrimaryKey(id);
    if (Objects.isNull(goods)) {
      return null;
    }
    MallGroup group = new MallGroup();

    group.setGroupId(goods.getGoodsId());
    group.setGroupName(goods.getGoodsName());
    group.setGroupIntro(goods.getGoodsIntro());
    group.setGroupCategoryId(goods.getGoodsCategoryId());
    group.setGroupCoverImg(goods.getGoodsCoverImg());
    group.setGroupDetailContent(goods.getGoodsDetailContent());
    group.setOriginalPrice(goods.getOriginalPrice());
    group.setTag(goods.getTag());

    return group;

  }

  /**
   * 查询商品的详细信息
   *
   * @param id
   * @return
   */
  @Override
  public MallGroup selectNewBeeMallGroup(Long id) {
    MallGroup group = groupMapper.selectByPrimaryKey(id);
    return group;
  }

  /**
   * 修改拼单商品
   *
   * @param mallGroup
   * @return
   */
  @Override
  public String updateNewBeeMallGroup(MallGroup mallGroup) {

    MallGroup group = groupMapper.selectByPrimaryKey(mallGroup.getGroupId());
//        判断id是否存在 存在修改 不存在增加
    if (group == null) {
//            不存在 增加拼团信息
      if (groupMapper.insertSelective(mallGroup) > 0) {
        return ServiceResultEnum.SUCCESS.getResult();
      }
      return ServiceResultEnum.DB_ERROR.getResult();
    }
    int i = 0;
    //若大文本未修改,调用updateByPrimaryKey
    if (group.getGroupDetailContent().equals(mallGroup.getGroupDetailContent())) {
      i = groupMapper.updateByPrimaryKey(mallGroup);
    } else {
      i = groupMapper.updateByPrimaryKeyWithBLOBs(mallGroup);
    }
    if (i > 0) {
//      System.out.println("ServiceResultEnum.SUCCESS.getResult() = " + ServiceResultEnum.SUCCESS.getResult());
      return ServiceResultEnum.SUCCESS.getResult();
    }
//    System.out.println("ServiceResultEnum.DB_ERROR.getResult() = " + ServiceResultEnum.DB_ERROR.getResult());
    return ServiceResultEnum.DB_ERROR.getResult();
  }

  /**
   * 批量修改商品的销售状态
   *
   * @param ids
   * @param sellStatus
   * @return
   */
  @Override
  public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
    return groupMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
  }

  /**
   * 判断是否已经是拼团商品
   *
   * @param id
   */
  @Override
  public Boolean selectExist(Long id) {
    Integer integer = groupMapper.selectCount(id);
//    System.out.println("--------" + integer);
    if (!Objects.isNull(integer)) {
      if (integer == 0) {
        return false;
      } else {
//                是拼团
        return true;
      }
    } else {
      return false;
    }


  }


}
