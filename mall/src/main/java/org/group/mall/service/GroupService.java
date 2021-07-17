package org.group.mall.service;

import org.group.mall.entity.MallGroup;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;

public interface GroupService {
    /**
     * 后台分页
     *
     * @param pageUtil
     * @return
     */
    PageResult getNewBeeMallGroupPage(PageQueryUtil pageUtil);

    /**
     * 添加新商品到拼团
     *
     * @param mallGroup
     * @return
     */
    String saveNewBeeMallGroup(MallGroup mallGroup);

    /**
     * 把已有商品封装成拼团商品
     * @param id
     * @return
     */
    MallGroup saveNewBeeMallGroupExist(Long id);

    /**
     * 查询商品的详细信息
     * @param id
     * @return
     */
    MallGroup selectNewBeeMallGroup(Long id);

    /**
     * 修改拼单商品
     * @param mallGroup
     * @return
     */
    String updateNewBeeMallGroup(MallGroup mallGroup);

    /**
     * 批量修改商品的销售状态
     * @param ids
     * @param sellStatus
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

    /**
     * 判断是否已经是拼团商品
     */
    Boolean selectExist(Long id);
}
