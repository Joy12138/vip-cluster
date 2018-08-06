///**
// * Alipay.com Inc.
// * Copyright (c) 2004-2016 All Rights Reserved.
// */
//package com.example;
//
//import com.alipay.api.AlipayApiException;
//import com.example.enums.AuthScope;
//import com.example.service.ActivityService;
//import com.example.service.MemberCardService;
//import com.example.service.PayService;
//import com.example.service.ShopService;
//import com.example.utils.RandomUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// *
// * @author jinlong.rhj
// * @version $Id: Tests.java, v 0.1 2016-10-19 17:07 jinlong.rhj Exp $$
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class TestService {
//
//    @Autowired
//    private MemberCardService memberCardService;
//
//    @Autowired
//    private ActivityService activityService;
//
//    @Autowired
//    private ShopService shopService;
//
//    @Autowired
//    private PayService payService;
//
//    @Test
//    public void testAuthScope(){
//        System.out.println(AuthScope.AUTH_ECARD.toString());
//    }
//
//    @Test
//    public void testQueryCard() throws AlipayApiException {
//        		String alipayCardNo ="test0000004753";
//        		memberCardService.queryCardByAlipayCardNo(alipayCardNo);
//    }
//
//
////    @Test
////    public void testActivity() throws AlipayApiException {
////       activityService.createActivity();
////
////        String activityId ="20161020000000000224102000151415";
////        activityService.modifyActivity(activityId);
////    }
//
//
//
//}