package com.w77996.pay.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.w77996.common.domain.BaseParam;
import com.w77996.common.enumm.RetEnum;
import com.w77996.common.util.JsonUtil;
import com.w77996.common.util.ObjectValidUtil;
import com.w77996.common.util.RpcUtil;
import com.w77996.pay.dao.model.MchInfo;
import com.w77996.pay.service.BaseService;
import com.w77996.pay.service.IMchInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 商户service实现类
 * @author: w77996
 * @create: 2018-10-11 13:14
 **/
@Service
@Slf4j
public class MchInfoServiceImpl extends BaseService implements IMchInfoService {


    @Override
    public Map selectMchInfo(String jsonParam) {
        BaseParam baseParam = JsonUtil.getObjectFromJson(jsonParam, BaseParam.class);
        Map<String, Object> bizParamMap = baseParam.getBizParamMap();
        if (ObjectValidUtil.isInvalid(bizParamMap)) {
            log.warn("查询商户信息失败, {}. jsonParam={}", RetEnum.RET_PARAM_NOT_FOUND.getMessage(), jsonParam);
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_PARAM_NOT_FOUND);
        }
        String mchId = baseParam.isNullValue("mchId") ? null : bizParamMap.get("mchId").toString();
        if (ObjectValidUtil.isInvalid(mchId)) {
            log.warn("查询商户信息失败, {}. jsonParam={}", RetEnum.RET_PARAM_INVALID.getMessage(), jsonParam);
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_PARAM_INVALID);
        }
        MchInfo mchInfo = super.baseSelectMchInfo(mchId);
        if (mchInfo == null) {
            return RpcUtil.createFailResult(baseParam, RetEnum.RET_BIZ_DATA_NOT_EXISTS);
        }
        String jsonResult = JsonUtil.object2Json(mchInfo);
        return RpcUtil.createBizResult(baseParam, jsonResult);
    }

    /**
     * 通过商户id获取商户信息
     *
     * @param mchId
     * @return
     */
    @Override
    public JSONObject getByMchId(String mchId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("mchId", mchId);
        String jsonParam = RpcUtil.createBaseParam(paramMap);
        Map<String, Object> result = this.selectMchInfo(jsonParam);
        String s = RpcUtil.mkRet(result);
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        return JsonUtil.getJSONObjectFromJson(s);
    }
}
