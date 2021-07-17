package org.group.mall.service.impl;

import org.group.mall.common.ServiceResultEnum;
import org.group.mall.controller.vo.NoticeUVO;
import org.group.mall.dao.MallNoticeMapper;
import org.group.mall.entity.Notice;
import org.group.mall.service.NoticeService;
import org.group.mall.util.PageQueryUtil;
import org.group.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class NoticeServiceImpl implements NoticeService {

  @Autowired
  private MallNoticeMapper noticeMapper;

  /**
   * 后台分页
   *
   * @param pageUtil
   * @return
   */
  @Override
  public PageResult getNewBeeMallNoticePage(PageQueryUtil pageUtil) {
    List<Notice> noticeList = noticeMapper.findNewBeeMallNoticeList(pageUtil);
    int total = noticeMapper.getTotalNewBeeMallNotice(pageUtil);
    PageResult pageResult = new PageResult(noticeList, total, pageUtil.getLimit(), pageUtil.getPage());
    return pageResult;
  }

  /**
   * 公告的撤回与发布
   *
   * @param ids
   * @param sellStatus
   * @return
   */
  @Override
  public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
    return noticeMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
  }

  /**
   * 公告的修改
   *
   * @param notice
   * @return
   */
  @Override
  public String updateNewBeeMallNotice(Notice notice) {
    Notice newBeeMallNotice = noticeMapper.selectByPrimaryKey(notice.getNoticeId());
    if (Objects.isNull(newBeeMallNotice)) {
      return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }
    notice.setUpdateTime(new Date());
    int i = 0;
    if (notice.getContext().equals(newBeeMallNotice.getContext())) {
      i = noticeMapper.updateByPrimaryKey(notice);
    } else {
      i = noticeMapper.updateByPrimaryKeyWithBLOBs(notice);
    }
    if (i > 0) {
      return ServiceResultEnum.SUCCESS.getResult();
    }
    return ServiceResultEnum.DB_ERROR.getResult();
  }

  /**
   * 公告的详细信息
   *
   * @param id
   * @return
   */
  @Override
  public Notice selectNewBeeMallNotice(Long id) {
    return noticeMapper.selectByPrimaryKey(id);
  }

  /**
   * 添加公告
   *
   * @param notice
   * @return
   */
  @Override
  public String saveNewBeeMallNotice(Notice notice) {
    notice.setUpdateTime(new Date());
    notice.setCreateTime(new Date());
    if (noticeMapper.insertSelective(notice) > 0) {
      return ServiceResultEnum.SUCCESS.getResult();
    }
    return ServiceResultEnum.DB_ERROR.getResult();
  }

  @Override
  public PageResult findNoticeList(PageQueryUtil pageUtil) {
    List<NoticeUVO> noticeUVOList = noticeMapper.findNoticeList(pageUtil);
    int total = noticeMapper.getTotalNotice(pageUtil);
    return new PageResult(noticeUVOList, total, pageUtil.getLimit(), pageUtil.getPage());
  }

  @Override
  public Notice selectNoticeById(Long id) {
    return noticeMapper.selectByPrimaryKey(id);
  }


}
