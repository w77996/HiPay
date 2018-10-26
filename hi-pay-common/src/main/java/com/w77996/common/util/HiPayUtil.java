package com.w77996.common.util;

import com.w77996.common.constant.PayConstant;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 支付工具类
 * @author: w77996
 * @create: 2018-10-26 13:35
 **/
@Slf4j
public class HiPayUtil {

    /**
     * 构建结果map
     * @param retCode
     * @param retMsg
     * @param resCode
     * @param errCode
     * @param errCodeDesc
     * @return
     */
    public static Map<String, Object> makeRetMap(String retCode, String retMsg, String resCode, String errCode, String errCodeDesc) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if(retCode != null) retMap.put(PayConstant.RETURN_PARAM_RETCODE, retCode);
        if(retMsg != null) retMap.put(PayConstant.RETURN_PARAM_RETMSG, retMsg);
        if(resCode != null) retMap.put(PayConstant.RESULT_PARAM_RESCODE, resCode);
        if(errCode != null) retMap.put(PayConstant.RESULT_PARAM_ERRCODE, errCode);
        if(errCodeDesc != null) retMap.put(PayConstant.RESULT_PARAM_ERRCODEDES, errCodeDesc);
        return retMap;
    }

    public static Map<String, Object> makeRetMap(String retCode, String retMsg, String resCode, PayEnum payEnum) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        if(retCode != null) retMap.put(PayConstant.RETURN_PARAM_RETCODE, retCode);
        if(retMsg != null) retMap.put(PayConstant.RETURN_PARAM_RETMSG, retMsg);
        if(resCode != null) retMap.put(PayConstant.RESULT_PARAM_RESCODE, resCode);
        if(payEnum != null) {
            retMap.put(PayConstant.RESULT_PARAM_ERRCODE, payEnum.getCode());
            retMap.put(PayConstant.RESULT_PARAM_ERRCODEDES, payEnum.getMessage());
        }
        return retMap;
    }

    /**
     * 验证支付中心签名
     * @param params
     * @return
     */
    public static boolean verifyPaySign(Map<String,Object> params, String key) {
        String sign = (String)params.get("sign"); // 签名
        params.remove("sign");	// 不参与签名
        String checkSign = PayDigestUtil.getSign(params, key);
        if (!checkSign.equalsIgnoreCase(sign)) {
            return false;
        }
        return true;
    }
}
