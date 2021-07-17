package org.group.mall.service.impl;

import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.IndexConfigGoodsVO;
import org.group.mall.dao.IndexConfigMapper;
import org.group.mall.dao.GoodsMapper;
import org.group.mall.entity.Goods;
import org.group.mall.entity.IndexConfig;
import org.group.mall.service.IndexConfigService;
import org.group.mall.util.BeanUtil;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {

  @Autowired
  private IndexConfigMapper indexConfigMapper;

  @Autowired
  private GoodsMapper goodsMapper;

  @Override
  public PageResult getConfigsPage(PageQueryUtil pageUtil) {
    List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
    int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
    PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
    return pageResult;
  }

  @Override
  public String saveIndexConfig(IndexConfig indexConfig) {
    //todo 判断是否存在该商品
    if (indexConfigMapper.insertSelective(indexConfig) > 0) {
      return ServiceResultEnum.SUCCESS.getResult();
    }
    return ServiceResultEnum.DB_ERROR.getResult();
  }

  @Override
  public String updateIndexConfig(IndexConfig indexConfig) {
    //todo 判断是否存在该商品
    IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
    if (temp == null) {
      return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }
    if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
      return ServiceResultEnum.SUCCESS.getResult();
    }
    return ServiceResultEnum.DB_ERROR.getResult();
  }

  @Override
  public IndexConfig getIndexConfigById(Long id) {
    return null;
  }

  @Override
  public List<IndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
    List<IndexConfigGoodsVO> indexConfigGoodsVOS = new ArrayList<>(number);
    List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
    if (!CollectionUtils.isEmpty(indexConfigs)) {
      //取出所有的goodsId
      List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
      List<Goods> goods = goodsMapper.selectByPrimaryKeys(goodsIds);
      indexConfigGoodsVOS = BeanUtil.copyList(goods, IndexConfigGoodsVO.class);
      for (IndexConfigGoodsVO indexConfigGoodsVO : indexConfigGoodsVOS) {
        String goodsName = indexConfigGoodsVO.getGoodsName();
        String goodsIntro = indexConfigGoodsVO.getGoodsIntro();
        // 字符串过长导致文字超出的问题
        if (goodsName.length() > 30) {
          goodsName = goodsName.substring(0, 30) + "...";
          indexConfigGoodsVO.setGoodsName(goodsName);
        }
        if (goodsIntro.length() > 22) {
          goodsIntro = goodsIntro.substring(0, 22) + "...";
          indexConfigGoodsVO.setGoodsIntro(goodsIntro);
        }
      }
    }
    return indexConfigGoodsVOS;
  }

  @Override
  public Boolean deleteBatch(Long[] ids) {
    if (ids.length < 1) {
      return false;
    }
    //删除数据
    return indexConfigMapper.deleteBatch(ids) > 0;
  }
}
