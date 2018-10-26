package com.w77996.pay.dao.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 商户实体类
 * @author: w77996
 * @create: 2018-10-25 13:43
 **/
@Data
public class MchInfo implements Serializable {

    /**
     * 商户ID
     *
     * @mbggenerated
     */
    private String mchId;

    /**
     * 名称
     *
     * @mbggenerated
     */
    private String name;

    /**
     * 类型
     *
     * @mbggenerated
     */
    private String type;

    /**
     * 请求私钥
     *
     * @mbggenerated
     */
    private String reqKey;

    /**
     * 响应私钥
     *
     * @mbggenerated
     */
    private String resKey;

    /**
     * 商户状态,0-停止使用,1-使用中
     *
     * @mbggenerated
     */
    private Byte state;

    /**
     * 创建时间
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * 更新时间
     *
     * @mbggenerated
     */
    private Date updateTime;
}
