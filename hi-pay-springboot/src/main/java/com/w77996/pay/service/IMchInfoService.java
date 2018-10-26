package com.w77996.pay.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 商户service
 */
public interface IMchInfoService {
    /**
     * 选择商户
     * @param jsonParam
     * @return
     */
    Map selectMchInfo(String jsonParam);

    /**
     * 选择商户json
     * @param mchId
     * @return
     */
    JSONObject getByMchId(String mchId);
}
