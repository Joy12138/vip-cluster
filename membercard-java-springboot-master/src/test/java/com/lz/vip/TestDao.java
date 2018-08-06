///**
// * Alipay.com Inc.
// * Copyright (c) 2004-2016 All Rights Reserved.
// */
//package com.example;
//
//import com.example.dao.CouponDao;
//import com.example.dao.MemberCardDao;
//import com.example.entity.MemberCard;
//import org.apache.commons.lang.builder.ReflectionToStringBuilder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// *
// * @author jinlong.rhj
// * @version $Id: TestDao.java, v 0.1 2016-11-09 15:43 jinlong.rhj Exp $$
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class TestDao {
//
//    @Autowired
//    private MemberCardDao mCardDao;
//
//    @Autowired
//    private CouponDao couponDao;
//
//    @Test
//    public void testActivity() throws  Exception {
//        couponDao.insertActivity("20161110000000000280507000151417");
//    }
//
//    @Test
//    public void testCardTemplate() throws  Exception {
//        mCardDao.insertTemplate("20161019000000000036105000300339");
//    }
//
//    @Test
//    public void testMembercard() throws Exception {
//        MemberCard mc = new MemberCard();
//        mc.setAlipayCardNo("2341234");
//        mc.setAlipayUserId("2341234");
//        mc.setCardTemplateId("345314123");
//        mc.setExternalCardNo("12341234");
//        mCardDao.insert(mc);
//
//        MemberCard mc2 = mCardDao.getLastOne();
//
//        System.out.println(ReflectionToStringBuilder.toString( mc2 ));
//    }
//}