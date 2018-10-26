package com.w77996.pay.dao;

import com.w77996.pay.dao.model.MchInfo;

/**
 * @description: 商户mapper
 * @author: w77996
 * @create: 2018-10-11 13:12
 **/
public interface MchInfoMapper {
    /**
     * 根据mchId获取商户信息
     * @param mchId
     * @return
     */
    public MchInfo selectByPrimaryKey(String mchId);
}
