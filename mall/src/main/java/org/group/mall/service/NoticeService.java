package org.group.mall.service;

import org.group.mall.entity.Notice;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;

public interface NoticeService {

  /**
   * 后台分页
   *
   * @param pageUtil
   * @return
   */
  PageResult getNewBeeMallNoticePage(PageQueryUtil pageUtil);

  /**
   * 公告的撤回与发布
   *
   * @param ids
   * @param sellStatus
   * @return
   */
  Boolean batchUpdateSellStatus(Long[] ids, int sellStatus);

  /**
   * 公告的修改
   *
   * @param notice
   * @return
   */
  String updateNewBeeMallNotice(Notice notice);

  /**
   * 公告的详细信息
   *
   * @param id
   * @return
   */
  Notice selectNewBeeMallNotice(Long id);

  /**
   * 添加公告
   *
   * @param notice
   * @return
   */
  String saveNewBeeMallNotice(Notice notice);


  /**
   * 分页查询公告信息
   *
   * @param pageUtil
   * @return
   */
  PageResult findNoticeList(PageQueryUtil pageUtil);

  /**
   * 根据id查
   *
   * @param id
   * @return
   */
  Notice selectNoticeById(Long id);
}
