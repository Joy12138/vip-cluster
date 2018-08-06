/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.lz.vip.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.lz.vip.config.AlipayConfig;
import com.lz.vip.utils.RandomUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 *
 */

//@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
//@ConfigurationProperties(prefix = "alipay.config")
@Component
public class PayService {

    private static final Logger logger = LoggerFactory.getLogger(PayService.class);

    @Autowired
    private AlipayConfig config;

    private DefaultAlipayClient alipayClient;

    @Autowired
    private MemberCardService memberCardService;

    @Autowired
    public void setAlipayClient(){
        this.alipayClient = new DefaultAlipayClient(config.getGateway(),
                config.getAppid(), config.getPrivateKey(), "json", "UTF-8",
                config.getAlipayPublicKey());
    }


//    private AlipayConfig alipayConfig;

    /**
     * 根据用户的userid创建订单,返回支付宝订单号,方便在手机端js唤起收银台支付
     * @param userid
     * @return
     * @throws AlipayApiException
     */
    public String orderCreate(Map<String, Object> map) throws AlipayApiException {

        AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        AlipayTradeCreateResponse response = alipayClient.execute(request);


        logger.info(response.getBody());
        if(response.isSuccess()){
            System.out.println("调用成功,返回的支付宝订单号:"+response.getTradeNo());
            return response.getTradeNo();
        } else {
            System.out.println("调用失败");
            return null;
        }
    }

    /**
     * 根据手机条码查出用户的userid
     * @param dynamicId
     * @throws AlipayApiException
     */
    public String queryUser(String dynamicId) throws AlipayApiException {

        AlipayMobileShakeUserQueryRequest request = new AlipayMobileShakeUserQueryRequest();
        request.setDynamicId(dynamicId);
        request.setDynamicIdType("bar_code");

        logger.info(dynamicId);

        AlipayMobileShakeUserQueryResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
        if (response.isSuccess()){
            return response.getUserId();
        }
        return "";
    }

    /**
     * 线下条码支付示例
     * @param dynamicId
     * @throws AlipayApiException
     */
    public String barCodePay(String dynamicId) throws AlipayApiException {


        List<HashMap<String, Object>> goodsDetailList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> goodsDetailMap = new HashMap<>();
        goodsDetailMap.put("goods_id", "COUPON");//商品的编号,标准商品请传国标码
        goodsDetailMap.put("goods_name", "来一桶方便嘛");//
        goodsDetailMap.put("quantity", "1");//
        goodsDetailMap.put("price", "0.01");//
        goodsDetailList.add(goodsDetailMap);

         
        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", RandomUtils.getRandomRequestId());//商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
        map.put("scene","bar_code");//支付场景 条码支付，取值：bar_code 声波支付，取值：wave_code
        map.put("auth_code",dynamicId);//支付授权码,扫到的条码
        map.put("subject","支付宝条码支付");//
        map.put("total_amount","0.01");//
        map.put("body","支付宝消费");
        map.put("store_id","2017030200077000000026866811");
        //map.put("terminal_id","341341");
        map.put("timeout_express","5m");
        map.put("goods_detail",goodsDetailList);
        System.out.println("dynamicId："+dynamicId);

        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        //请根据返回的支付状态,判断入账还是轮询查询
        AlipayTradePayResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
        return response.getTradeNo();
    }

    /**
     * 手机扫二维码支付
     * @throws AlipayApiException
     */
    public void qrCodePay() throws AlipayApiException{
        List<HashMap<String, Object>> goodsDetailList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> goodsDetailMap = new HashMap<>();
        goodsDetailMap.put("goods_id", "23413412323");//商品的编号,标准商品请传国标码
        goodsDetailMap.put("goods_name", "来一桶方便嘛");//
        goodsDetailMap.put("quantity", "1");//
        goodsDetailMap.put("price", "3.50");//
        goodsDetailList.add(goodsDetailMap);


        HashMap<String, Object> subMerchantMap = new HashMap<>();
        subMerchantMap.put("merchant_id","16360680352");

        Map<String, Object> map = new HashMap<>();
        map.put("out_trade_no", RandomUtils.getRandomRequestId());//商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复
        map.put("subject","支付宝二维码支付");//
        map.put("total_amount","0.01");//
        map.put("body","支付宝消费");
        map.put("store_id","12320");
        map.put("terminal_id","341341");
        map.put("timeout_express","5m");
        map.put("goods_detail",goodsDetailList);
        map.put("sub_merchant",subMerchantMap);


        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setBizContent(JSON.toJSONString(map));
//        request.setReturnUrl("");//设定异步通知地址

        logger.info(request.getBizContent());

        //请根据返回的支付状态,判断入账还是轮询查询
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
    }



    /**
     * 支付核销一体化,扫条码后先核销会员卡再支付
     * @param dynamicId
     * @throws AlipayApiException
     */
    public void payWithMemberCard1(String dynamicId, Map<String, Object> map) throws AlipayApiException {
        //收银员扫码后,先拿条码去查用户ID
        String userId= queryUser(dynamicId);
        //根据用户ID去查本地存储的用户会员卡
        String alipayCardNo = "";//通过本地查出

        if (!StringUtils.isEmpty(alipayCardNo)){
            //查到有绑定支付宝会员卡


            //扣减会员卡的相关权益


            //修改价格,再发起条码支付

        }


        //支付
        barCodePay(dynamicId);

        //根据会员卡号,更新用户支付宝卡包中的会员卡消费记录
        memberCardService.syncCard(map);

    }

    /**
     * 核销卡权益后支付
     */
    public void memberCardConsumeAndPay1(String cardDBarCode,String dynamicId, Map<String, Object> map)
            throws AlipayApiException {

        //先扫描会员卡条码,查出会员卡信息
        memberCardService.queryCardByBarCode(cardDBarCode);
        //核销会员卡权益

        //修改价格,发起支付

        barCodePay(dynamicId);

        //根据会员卡号,更新用户支付宝卡包中的会员卡消费记录
        memberCardService.syncCard(map);
    }

    /**
     * 根据订单号进行退款方便支付测试后进行退款
     * @param tradeNo
     * @return
     * @throws AlipayApiException
     */
    public String tradeRefund(String tradeNo,String refundAmount) throws AlipayApiException {

        Map<String, Object> map = new HashMap<>();
        map.put("trade_no", tradeNo);
       
        map.put("refund_amount",refundAmount);

        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        AlipayTradeRefundResponse response = alipayClient.execute(request);


        logger.info(response.getBody());
        if(response.isSuccess()){
            System.out.println("退款成功，本次退款金额:"+response.getRefundFee());
            return response.getRefundFee();
        } else {
            System.out.println("退款失败");
            return null;
        }
    }

}