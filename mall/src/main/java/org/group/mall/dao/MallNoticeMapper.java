package org.group.mall.dao;

import org.group.mall.controller.vo.NoticeUVO;
import org.group.mall.entity.Notice;
import org.group.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallNoticeMapper {
  /**
   * 分页查询公告信息,无大文本
   *
   * @param pageUtil
   * @return
   */
  List<Notice> findNewBeeMallNoticeList(PageQueryUtil pageUtil);

  /**
   * 分页查询公告信息,无大文本
   *
   * @param pageUtil
   * @return
   */
  List<NoticeUVO> findNoticeList(PageQueryUtil pageUtil);

  /**
   * 查询总条数
   *
   * @param pageUtil
   * @return
   */
  int getTotalNewBeeMallNotice(PageQueryUtil pageUtil);

  /**
   * 查询总条数
   *
   * @param pageUtil
   * @return
   */
  int getTotalNotice(PageQueryUtil pageUtil);

  /**
   * 通过id查询公告信息 有大文本
   *
   * @param id
   * @return
   */
  Notice selectByPrimaryKey(Long id);

  /**
   * 添加公告信息
   *
   * @param notice
   * @return
   */
  int insertSelective(Notice notice);

  /**
   * 批量更改状态
   */
  int batchUpdateSellStatus(@Param("orderIds") Long[] orderIds, @Param("sellStatus") int sellStatus);

  /**
   * 修改 包含大文本
   *
   * @param notice
   * @return
   */
  int updateByPrimaryKeyWithBLOBs(Notice notice);

  /**
   * 修改 不包含大文本
   *
   * @param notice
   * @return
   */
  int updateByPrimaryKey(Notice notice);


}
