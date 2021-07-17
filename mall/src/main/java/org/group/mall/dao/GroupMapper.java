package org.group.mall.dao;

import org.group.mall.entity.MallGroup;
import org.group.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface GroupMapper {

    /**
     * 分页查询拼单信息,无细节
     * @param pageUtil
     * @return
     */
    List<MallGroup> findNewBeeMallGRoupList(PageQueryUtil pageUtil);

    /**
     * 查询总条数
     * @param pageUtil
     * @return
     */
    int getTotalNewBeeMallGroup(PageQueryUtil pageUtil);

    /**
     * 添加拼团商品
     * @param group
     * @return
     */
    int insertSelective(MallGroup group);

    /**
     * 查询商品的详细信息,有细节
     */
    MallGroup selectByPrimaryKey(Long id);

    /**
     * 批量更改状态
     */
    int batchUpdateSellStatus(@Param("orderIds") Long[] orderIds, @Param("sellStatus") int sellStatus);

    /**
     * 修改 包含大文本
     * @param record
     * @return
     */
    int updateByPrimaryKeyWithBLOBs(MallGroup record);

    /**
     * 修改 不包含大文本
     * @param record
     * @return
     */
    int updateByPrimaryKey(MallGroup record);


    /**
     * 查询id是否存在
     * @param id
     * @return
     */
    Integer selectCount(Long id);
}
