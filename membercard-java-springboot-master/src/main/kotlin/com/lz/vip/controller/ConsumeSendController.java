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
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// *
// */
//@Controller
//public class ConsumeSendController {
//    @Autowired
//    private PayService   payService;
//    @Autowired
//    private AlipayConfig config;
//    @Autowired
//    private AuthService  authService;
//    @Autowired
//    private ActivityService activityService;
//    @Autowired
//    private Environment  env;
//
//    private static final Logger logger = LoggerFactory.getLogger(AlipayRefundController.class);
//
//    @RequestMapping(value = "/consumesend")
//    public String auth(Model model) {
//        String returnUrl = config.getUrl() + "/consumecode";
////        String returnUrl = "http://max.ngrok.lxwgo.com/consumecode";
//
//        String authUrl = "";
//        try {
//            authUrl = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=" + config
//                    .getAppid() + "&scope=auth_base&redirect_uri=" + URLEncoder
//                              .encode(returnUrl, "UTF-8");
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return "redirect:" + authUrl;
//    }
//
//    @RequestMapping(value = "/consumecode")
//    public String tradeCreate(HttpServletRequest request, Model model) {
//        String authCode = request.getParameter("auth_code");
//        System.out.println("authCode:" + authCode);
//        String userid = null;
//        try {
//            userid = authService.getUserId(authCode);
//            System.out.println(userid);
//
//            List<HashMap<String, Object>> goodsDetailList = new ArrayList<HashMap<String, Object>>();
//            HashMap<String, Object> goodsDetailMap = new HashMap<>();
//            goodsDetailMap.put("goods_id", "itemtestrisk10");//商品的编号,标准商品请传国标码
//            goodsDetailMap.put("goods_name", "单品测试");//
//            goodsDetailMap.put("quantity", "1");//
//            goodsDetailMap.put("price", "0.01");//
//            goodsDetailList.add(goodsDetailMap);
//
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("out_trade_no", RandomUtils.getRandomRequestId());
//            map.put("buyer_id",userid);//支付授权码,扫到的条码
//            map.put("subject","统一支付");//
//            map.put("total_amount","0.1");//
//            map.put("body","消费送测试");
//            map.put("alipay_store_id","2017030200077000000026866811");
//            map.put("timeout_express","5m");
//            map.put("goods_detail",goodsDetailList);
//
//            String tradeNO = payService.orderCreate(map);
//            model.addAttribute("tradeNO", tradeNO);
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        return "cs_alipay_h5";
//    }
//
//    /**
//     * 创建消费送营销活动，只需创建一次
//     * @param
//     * @throws AlipayApiException
//     */
//    public String  createConsumeSendActivity() throws AlipayApiException
//    {
//      HashMap<String, Object> budgetInfoMap = new HashMap<>();
//      budgetInfoMap.put("budget_type", "QUANTITY");
//      budgetInfoMap.put("budget_total","10000");
//
//      HashMap<String, Object> constraintInfoMap = new HashMap<>();
//      constraintInfoMap.put("user_win_count", "100");
//      constraintInfoMap.put("user_win_frequency","D||100");
//      constraintInfoMap.put("suit_shops", new String[] { "2017030200077000000026866811" });
//
//      HashMap<String, Object> useRuleMap = new HashMap<>();
//      useRuleMap.put("min_consume", "0.01");
//      useRuleMap.put("suit_shops", new String[] { "2016071500077000000017624383" });
//
//      HashMap<String, Object> voucherMap = new HashMap<>();
//      voucherMap.put("type", "MONEY");
//      voucherMap.put("voucher_note", "抵扣0.01元现金");
//      voucherMap.put("name", "1分钱代金券");
//      voucherMap.put("brand_name", "消费送测试");
//      voucherMap.put("use_instructions", new String[] { "1.在2017030200077000000026866811门店可用", "2.消费满0.1元可用" });
//      voucherMap.put("logo", "1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC");
//      voucherMap.put("voucher_img", "1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC");
//      voucherMap.put("validate_type", "FIXED");
//      voucherMap.put("start_time", "2017-03-01 00:00:00");
//      voucherMap.put("end_time", "2017-12-30 00:00:00");
//      voucherMap.put("use_rule", useRuleMap);
//      voucherMap.put("effect_type",
//              "IMMEDIATELY");//券生效的方式，目前支持以下方式 立即生效：IMMEDIATELY 延迟生效：DELAY 仅在券有效期类型为相对有效期时生效
//      voucherMap.put("worth_value",
//              "0.01");//券面额，单位元 必须为合法金额类型字符串 券类型为代金券、减至券时，券面额必须设置 单品减至券的券面额必须低于单品原价
//      voucherMap.put("desc", "券的详细说明");
//
//      HashMap<String, Object> sendRuleMap = new HashMap<>();
//      sendRuleMap.put("min_cost", "0.1");
//      sendRuleMap.put("send_num", "1");
//
//      List<HashMap<String, Object>> promoToolsList = new ArrayList<>();
//      HashMap<String, Object> promoToolsMap = new HashMap<>();
//      promoToolsMap.put("voucher", voucherMap);
//      promoToolsList.add(promoToolsMap);
//
//      List<HashMap<String, Object>> publishChannelsList = new ArrayList<>();
//      //        HashMap<String,Object> publishChannelsMap = new HashMap<>();
//      //        publishChannelsMap.put("type","SHORT_LINK");
//      //        publishChannelsMap.put("name","短连接投放");
//      //        publishChannelsList.add(publishChannelsMap);
//
//      //        HashMap<String,Object> publishChannelsMap2 = new HashMap<>();
//      //        publishChannelsMap2.put("type","SHOP_DETAIL");
//      //        publishChannelsMap2.put("name","投放到店铺");
//      //        publishChannelsList.add(publishChannelsMap2);
//
////      HashMap<String, Object> publishChannelsMap3 = new HashMap<>();
////      publishChannelsMap3.put("type", "URL_WITH_TOKEN");
////      publishChannelsMap3.put("name", "短连接带token投放");
////      publishChannelsList.add(publishChannelsMap3);
//
//      //        HashMap<String,Object> extInfoMap = new HashMap<>();
//      //        extInfoMap.put("MEMBER_FILTER_TYPE","MEMBER");
//
//      Map<String, Object> map = new HashMap<>();
//      map.put("out_biz_no", RandomUtils.getRandomRequestId());//
//      map.put("name", "消费送测试");//
//      map.put("start_time", "2017-03-01 00:00:00");//
//      map.put("end_time", "2017-12-30 00:00:00");//
//      map.put("type", "CONSUME_SEND");//
//      map.put("desc", "消费送测试");//
//      map.put("budge_info", budgetInfoMap);
//      map.put("constraint_info", constraintInfoMap);//
//      map.put("promo_tools", promoToolsList);//
//      //map.put("publish_channels", publishChannelsList);//
//      KoubeiMarketingCampaignActivityCreateResponse response=activityService.createActivity(map);
//	  return response.getCampId();
//
//    }
//}