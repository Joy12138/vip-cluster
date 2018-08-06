///**
// * Alipay.com Inc.
// * Copyright (c) 2004-2016 All Rights Reserved.
// */
//package com.example.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.internal.util.AlipaySignature;
//import com.example.config.AlipayConfig;
//import com.example.dao.CouponDao;
//import com.example.entity.Activity;
//import com.example.entity.Coupon;
//import org.apache.commons.lang.builder.ReflectionToStringBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Enumeration;
//import java.util.Map;
//import java.util.TreeMap;
//
///**
// *
// */
//@Controller
//@RequestMapping("/alipay/notify/gateway")
//public class AlipayNotifyGatewayController {
//
//    @Autowired
//    private AlipayConfig config;
//
//    @Autowired
//    private CouponDao couponDao;
//
//    private static final Logger logger = LoggerFactory
//            .getLogger(AlipayNotifyGatewayController.class);
//
//    /**
//     * 创建活动和领券成功,都会向这个地址发送异步通知.前提是创建活动的时候,有将这个地址的公网URL传递给创建活动接口的notify_url公共参数中.
//     * <p>
//     * 创建活动成功收到的异步通知示例:
//     * http://jinlong.ngrok.sapronlee.com/alipay/notify/gateway/activity.do?name=1分钱吃货优惠&id=20161110000000000280507000151417&order_type=ENABLE&sign=***&order_process_time=2016-11-10 10:12:38&status=STARTING&event_code=EC-CAMPAIGN_PROCESS-CAMPAIGN_SYNCH&order_status=SUCCESS&order_id=20161110000000000831517000155416
//     * <p>
//     * 领券成功收到的异步通知示例:
//     * http://jinlong.ngrok.sapronlee.com/activity/notify/gateway/create_activity.html?buyer_id=2088702052268530&merchant_uid=2088911212445410&notify_time=2016-11-10 13:45:34&isv_out_biz_id=2016111010191200686657&sign_type=RSA&charset=UTF-8&auth_app_id=2016070801594557&notify_type=koubei_receive_coupon_notify&business_order=2088702052268530|20161110000000000280507000151417&version=1.0&sign=***&id=20161110000000000280507000151417&event_code=EC-promocore-prize-notice&app_id=2016070801594557&notify_id=0d0016875b412e9ad109e018bf98748lmu
//     *
//     * @param request
//     * @return
//     * @throws Exception
//     */
//    @PostMapping("/activity.do")
//    @ResponseBody
//    public String createActivity(HttpServletRequest request) throws Exception {
//        Enumeration<String> enumeration = request.getParameterNames();
//        Map<String, String> map = new TreeMap<>();
//        while (enumeration.hasMoreElements()) {
//            String key = enumeration.nextElement();
//            map.put(key, request.getParameter(key));
//            logger.info(key);
//        }
//        logger.info(ReflectionToStringBuilder.toString(map));
//        String sign = map.get("sign");
//        String signType = StringUtils.isEmpty(map.get("sign_type")) ? "RSA" : map.get("sign_type");
//        map.remove("sign");
//        map.remove("sign_type");
//        String content = AlipaySignature.getSignContent(map);
//
//        //        for (String key : map.keySet()){
//        //            content+= key + "=" + map.get(key);
//        //        }
//        boolean isSignedChecked = AlipaySignature
//                .rsaCheck(content, sign, config.getAlipayPublicKey(), "utf-8", signType);
//        if (isSignedChecked) {
//            logger.info(JSON.toJSONString(map));
//        }
//
//        //领券通知
//        if (map != null && "koubei_receive_coupon_notify".equalsIgnoreCase(map.get("notify_type"))) {
//            Coupon coupon = new Coupon();
//            coupon.setMerchantUid(map.get("merchant_uid"));
//            coupon.setPrizeId(map.get("id"));
//            coupon.setOutBizNo(map.get("2016111010191200686657"));
//            coupon.setBuyerId(map.get("buyer_id"));
//            couponDao.insert(coupon);
//        }
//
//        //接口要求,处理成功后,返回"success",否则25小时内会发8次重试
//        return "success";
//    }
//
//}