package com.w77996.pay.service;

import com.w77996.pay.dao.MchInfoMapper;
import com.w77996.pay.dao.model.MchInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: service基类
 * @author: w77996
 * @create: 2018-10-11 13:04
 **/
@Service
public class BaseService {

    @Autowired
    private MchInfoMapper mchInfoMapper;

    /**
     * 选择商户信息
     * @param mchId
     * @return
     */
    public MchInfo baseSelectMchInfo(String mchId) {
        return mchInfoMapper.selectByPrimaryKey(mchId);
    }
}
