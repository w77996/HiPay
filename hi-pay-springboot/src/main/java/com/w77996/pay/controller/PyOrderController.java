package com.w77996.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.w77996.common.constant.PayConstant;
import com.w77996.common.util.HiPayUtil;
import com.w77996.common.util.MySeq;
import com.w77996.pay.service.IMchInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;

/**
 * 支付订单controller
 * @author  w77996
 * @date 2018/10/10 13:10
 */
@Slf4j
@RestController
public class PyOrderController {


    @Autowired
    private IMchInfoService mchInfoService;
    /**
     * 统一下单接口:
     * 1)先验证接口参数以及签名信息
     * 2)验证通过创建支付订单
     * 3)根据商户选择渠道,调用支付服务进行下单
     * 4)返回下单数据
     * @param params
     * @return
     */
    @RequestMapping("/api/pay/create_order")
    public String payOrder(@RequestParam String params){
        JSONObject po = JSONObject.parseObject(params);
        return payOrder(po);
    }

    @RequestMapping(value = "/api/pay/create_order",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    public String payOrder(@RequestBody JSONObject params){
        log.info("###### 开始接收商户统一下单请求 ######");
        try {
            JSONObject payContext = new JSONObject();
            JSONObject payOrder = null;
            //校验参数
            Object object = validateParams(params,payContext);

        }catch (Exception e){

        }
        return null;
    }
    /**
     * 验证创建订单请求参数,参数通过返回JSONObject对象,否则返回错误文本信息
     * @param params
     * @return
     */
    private Object validateParams(JSONObject params, JSONObject payContext) {

        String errorMessage;

        //支付参数
        String mchId = params.getString("mchId"); //商户id
        String mchOrderNo = params.getString("mchOrderNo"); // 商戶
        String channelId = params.getString("channelId"); //渠道ID WX_NATIVE,ALIPAY_WAP
        String amount = params.getString("amount"); //支付金额，单位分
        String currency = params.getString("currency"); // 币种
        String clientIp = params.getString("clientIp");//客户端Ip
        String device  = params.getString("device");//设备
        String extra = params.getString("extra");//特定渠道发起时额外的参数
        String param1 = params.getString("param1");
        String param2 = params.getString("param2");
        String notifyUrl = params.getString("notifyUrl"); 		// 支付结果回调URL
        String sign = params.getString("sign"); 				// 签名
        String subject = params.getString("subject");	        // 商品主题
        String body = params.getString("body");	                // 商品描述信息

        // 验证请求参数有效性（必选项）
        if(StringUtils.isBlank(mchId)) {
            errorMessage = "request params[mchId] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(mchOrderNo)) {
            errorMessage = "request params[mchOrderNo] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(channelId)) {
            errorMessage = "request params[channelId] error.";
            return errorMessage;
        }
        if(!NumberUtils.isNumber(amount)) {
            errorMessage = "request params[amount] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(currency)) {
            errorMessage = "request params[currency] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(notifyUrl)) {
            errorMessage = "request params[notifyUrl] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(subject)) {
            errorMessage = "request params[subject] error.";
            return errorMessage;
        }
        if(StringUtils.isBlank(body)) {
            errorMessage = "request params[body] error.";
            return errorMessage;
        }


        // 根据不同渠道,判断extra参数
        if(PayConstant.PAY_CHANNEL_WX_JSAPI.equalsIgnoreCase(channelId)) {//微信公众号支付
            if(StringUtils.isEmpty(extra)) {
                errorMessage = "request params[extra] error.";
                return errorMessage;
            }
            JSONObject extraObject = JSON.parseObject(extra);
            String openId = extraObject.getString("openId");
            if(StringUtils.isBlank(openId)) {
                errorMessage = "request params[extra.openId] error.";
                return errorMessage;
            }
        }else if(PayConstant.PAY_CHANNEL_WX_NATIVE.equalsIgnoreCase(channelId)) {//微信扫码支付
            if(StringUtils.isEmpty(extra)) {
                errorMessage = "request params[extra] error.";
                return errorMessage;
            }
            JSONObject extraObject = JSON.parseObject(extra);
            String productId = extraObject.getString("productId");
            if(StringUtils.isBlank(productId)) {
                errorMessage = "request params[extra.productId] error.";
                return errorMessage;
            }
        }else if(PayConstant.PAY_CHANNEL_WX_MWEB.equalsIgnoreCase(channelId)) {//微信H5支付
            if(StringUtils.isEmpty(extra)) {
                errorMessage = "request params[extra] error.";
                return errorMessage;
            }
            JSONObject extraObject = JSON.parseObject(extra);
            String productId = extraObject.getString("sceneInfo");
            if(StringUtils.isBlank(productId)) {
                errorMessage = "request params[extra.sceneInfo] error.";
                return errorMessage;
            }
            if(StringUtils.isBlank(clientIp)) {
                errorMessage = "request params[clientIp] error.";
                return errorMessage;
            }
        }

        // 签名信息
        if (StringUtils.isEmpty(sign)) {
            errorMessage = "request params[sign] error.";
            return errorMessage;
        }
        //查询商户信息
        JSONObject mchInfo = mchInfoService.getByMchId(mchId);
        if(mchInfo == null) {
            errorMessage = "Can't found mchInfo[mchId="+mchId+"] record in db.";
            return errorMessage;
        }
        if(mchInfo.getByte("state") != 1) {
            errorMessage = "mchInfo not available [mchId="+mchId+"] record in db.";
            return errorMessage;
        }
        String reqKey = mchInfo.getString("reqKey");
        if (StringUtils.isBlank(reqKey)) {
            errorMessage = "reqKey is null[mchId="+mchId+"] record in db.";
            return errorMessage;
        }
        payContext.put("resKey", mchInfo.getString("resKey"));

        // 查询商户对应的支付渠道
//        JSONObject payChannel = payChannelService.getByMchIdAndChannelId(mchId, channelId);
//        if(payChannel == null) {
//            errorMessage = "Can't found payChannel[channelId="+channelId+",mchId="+mchId+"] record in db.";
//            return errorMessage;
//        }
//        if(payChannel.getByte("state") != 1) {
//            errorMessage = "channel not available [channelId="+channelId+",mchId="+mchId+"]";
//            return errorMessage;
//        }
        // 验证签名数据
        boolean verifyFlag = HiPayUtil.verifyPaySign(params, reqKey);
        if(!verifyFlag) {
            errorMessage = "Verify XX pay sign failed.";
            return errorMessage;
        }
        // 验证参数通过,返回JSONObject对象
        JSONObject payOrder = new JSONObject();
        payOrder.put("payOrderId", MySeq.getPay());
        payOrder.put("mchId", mchId);
        payOrder.put("mchOrderNo", mchOrderNo);
        payOrder.put("channelId", channelId);
        payOrder.put("amount", Long.parseLong(amount));
        payOrder.put("currency", currency);
        payOrder.put("clientIp", clientIp);
        payOrder.put("device", device);
        payOrder.put("subject", subject);
        payOrder.put("body", body);
        payOrder.put("extra", extra);
        payOrder.put("channelMchId", payChannel.getString("channelMchId"));
        payOrder.put("param1", param1);
        payOrder.put("param2", param2);
        payOrder.put("notifyUrl", notifyUrl);
        return payOrder;
    }
}
