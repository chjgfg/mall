package org.group.mall.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class GroupToRedis implements Serializable {

    private Integer redis_id;
    private Long group_id;
    private String group_work_id;
    private Long user_id;
}