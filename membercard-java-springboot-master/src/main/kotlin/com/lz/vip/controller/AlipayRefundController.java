///**
// * Alipay.com Inc.
// * Copyright (c) 2004-2016 All Rights Reserved.
// */
//package com.example.controller;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alipay.api.AlipayApiException;
//import com.alipay.api.domain.CampDetail;
//import com.alipay.api.domain.PublishChannel;
//import com.alipay.api.request.KoubeiMarketingCampaignActivityCreateRequest;
//import com.alipay.api.response.*;
//import com.example.config.AlipayConfig;
//import com.example.dao.CouponDao;
//import com.example.dao.MemberCardDao;
//import com.example.entity.Activity;
//import com.example.entity.CardTemplate;
//import com.example.entity.MemberCard;
//import com.example.enums.AuthScope;
//import com.example.service.ActivityService;
//import com.example.service.AuthService;
//import com.example.service.MemberCardService;
//import com.example.service.PayService;
//import com.example.utils.RandomUtils;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.Mapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletRequest;
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// *
// */
//@Controller
//public class AlipayRefundController {
//    @Autowired
//    private PayService   payService;
//    @Autowired
//    private AlipayConfig config;
//    @Autowired
//    private AuthService  authService;
//    @Autowired
//    private Environment  env;
//
//    private static final Logger logger = LoggerFactory.getLogger(AlipayRefundController.class);
//
//    @RequestMapping("/refund.html")
//    public String index() {
//        return "refund";
//    }
//
//    @RequestMapping(value = "/refundtrade")
//    @ResponseBody
//    public String tradeRefund(HttpServletRequest request, Model model) {
//        String tradeNo = request.getParameter("trade_no");
//        String refundAmount = request.getParameter("refund_amount");
//        String refundfee = null;
//        System.out.println("tradeNo:" + tradeNo);
//        logger.info("tradeNo:" + tradeNo);
//        logger.info("refund_amount:" + refundAmount);
//        String userid = null;
//        try {
//            refundfee = payService.tradeRefund(tradeNo,refundAmount);
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        return "本次退款："+refundfee;
//    }
//}