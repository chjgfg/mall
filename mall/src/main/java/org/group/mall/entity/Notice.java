package org.group.mall.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Notice {

    private Long noticeId;

    private String context;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    private Integer status;

    private String title;

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public String getContext() {
        return context;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Integer getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }
}
