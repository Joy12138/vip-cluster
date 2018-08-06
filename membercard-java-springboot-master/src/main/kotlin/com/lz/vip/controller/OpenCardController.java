package com.lz.vip.controller;

//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.alipay.api.domain.CampDetail;
//import com.alipay.api.domain.PublishChannel;
//import com.alipay.api.request.KoubeiMarketingCampaignActivityCreateRequest;
//import com.example.config.AlipayConfig;
//import com.example.dao.CouponDao;
//import com.example.dao.MemberCardDao;
//import com.example.entity.Activity;

import com.alipay.api.response.*;
import com.lz.vip.domainmodel.CardTemplate;
import com.lz.vip.entity.MemberCard;
import com.alipay.api.AlipayApiException;
import com.lz.vip.enums.AuthScope;
//import com.example.service.ActivityService;
import com.lz.vip.service.AuthService;
import com.lz.vip.service.MemberCardService;
//import com.example.service.PayService;
import com.lz.vip.utils.RandomUtils;
import com.lz.vip.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//

/**
 *
 */
@Controller
public class OpenCardController {
    //    @Autowired
//    private PayService   payService;
//    @Autowired
//    private AlipayConfig config;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private UserService userService;
//    @Autowired
//    private Environment  env;
//
//
//    @Autowired
//    private CouponDao couponDao;
//
//    @Autowired
//    private MemberCardDao cardDao;

    private AlipaySystemOauthTokenResponse tokenResponse = null;

    private static final Logger logger = LoggerFactory.getLogger(OpenCardController.class);
//
//    @RequestMapping(value = "/oc_h5pay.html")
//    public String oc_tradeCreate(HttpServletRequest request, Model model) {
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
//			Map<String, Object> map = new HashMap<>();
//            map.put("out_trade_no", RandomUtils.getRandomRequestId());
//            map.put("buyer_id",userid);//支付授权码,扫到的条码
//            map.put("subject","统一支付");//
//            map.put("total_amount","0.1");//
//            map.put("body","消费送测试");
//            map.put("store_id","2017030200077000000026866811");
//            map.put("timeout_express","5m");
//            map.put("goods_detail",goodsDetailList);
//
//            String tradeNO = payService.orderCreate(map);
//            model.addAttribute("tradeNO", tradeNO);
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        return "oc_alipay_h5";
//    }
//
//    @RequestMapping("/")
//    public String index() {
//        return "index";
//    }
//
//    @RequestMapping(value = "/oc_pay_auth")
//    public String oc_auth(Model model) {
//        String returnUrl = config.getUrl() + ":" + config.getPort() + "/oc_h5pay.html";
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
//    /**
//     * 用于授权回调使用
//     * @param request
//     * @return
//     */
//    @RequestMapping(value = "/oc_auth.html")
//    @ResponseBody
//    public String oc_getUserId(HttpServletRequest request) {
//        String authCode = request.getParameter("auth_code");
//        System.out.println("authCode:" + authCode);
//        String userid = null;
//        try {
//            userid = authService.getUserId(authCode);
//            System.out.println(userid);
//        } catch (AlipayApiException e) {
//            e.printStackTrace();
//        }
//        return userid;
//    }
//

    /**
     * 用户信息页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/oc_userinfo.html")
    public String oc_userInfo(HttpServletRequest request, Model model) throws Exception {
        String authCode = request.getParameter("auth_code");
        logger.debug("authCode:" + authCode);
        //如果没有授权,就跳转去授权,这个回调地址必须要配置到支付宝这个appid下,不然跳不回来
        if (StringUtils.isEmpty(authCode)) {
            String returnUrl = request.getRequestURL().toString();
            String authUrl = authService.getAuthUrl(returnUrl, AuthScope.AUTH_ECARD, AuthScope.AUTH_USER);
            return "redirect:" + authUrl;
        }
        AlipayUserInfoShareResponse userinfoShareResponse = null;
        try {
            tokenResponse = authService.getTokenResponse(authCode);
            logger.info(tokenResponse.getBody());
            logger.info(tokenResponse.getAccessToken());

            if (!StringUtils.isEmpty(tokenResponse.getUserId()) && !StringUtils
                    .isEmpty(tokenResponse.getAccessToken())) {

                userinfoShareResponse = memberCardService
                        .getUserInfo(tokenResponse.getAccessToken());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        model.addAttribute("notice", "出现错误,请检查日志");
        if (userinfoShareResponse != null && userinfoShareResponse.isSuccess()) {
            model.addAttribute("notice", "获取用户信息成功");

            model.addAttribute("response", "userId = " + userinfoShareResponse.getUserId() + "\r\n" +
                    "userName = " + userinfoShareResponse.getUserName() + "\r\n" +
                    "mobile = " + userinfoShareResponse.getMobile() + "\r\n" +
                    "city = " + userinfoShareResponse.getCity() + "\r\n" +
                    "province = " + userinfoShareResponse.getProvince());
            model.addAttribute("username", userinfoShareResponse.getUserId());
            model.addAttribute("mobile", userinfoShareResponse.getCity());
            logger.info("getUserId: {} ", userinfoShareResponse.getUserId());
            logger.info("getCity: {} ", userinfoShareResponse.getCity());
            logger.info("userName: {}", userinfoShareResponse.getUserName());
            logger.info("gender: {}", userinfoShareResponse.getGender());
            userService.initUserStaticInfo(userinfoShareResponse.getUserId());
        }
        return "oc_userinfo";
    }

    /**
     * 用户开卡页面
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/oc_opencard.html")
    public String oc_openCard(HttpServletRequest request, Model model) throws Exception {
        String cardTemplateId = "";

        //本地库中没有卡模板,就创建一个卡模板
        CardTemplate cardTemplate = userService.getLastTemplate();
        if (cardTemplate != null && !StringUtils.isEmpty(cardTemplate.getCardTemplateId())) {
            cardTemplateId = cardTemplate.getCardTemplateId();
        } else {
            AlipayMarketingCardTemplateCreateResponse response = memberCardService
                    .createCardTemplate(getCardTemplateMap());
            if (response.isSuccess() && response.getTemplateId() != null) {
                cardTemplateId = response.getTemplateId();
            }
        }
        //卡模版数据存储
        userService.insertTemplate(cardTemplateId);
        AlipayMarketingCardOpenResponse cardOpenResponse = null;
        try {

            if (!StringUtils.isEmpty(tokenResponse.getUserId()) && !StringUtils
                    .isEmpty(tokenResponse.getAccessToken())) {
                cardOpenResponse = memberCardService
                        .openCard(cardTemplateId, getCardInfoMap(tokenResponse.getUserId()), tokenResponse.getAccessToken());
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        model.addAttribute("notice", "出现错误,请检查日志");
        if (cardOpenResponse != null && cardOpenResponse.isSuccess()) {
            model.addAttribute("response", "支付宝会员卡号：" + cardOpenResponse.getCardInfo().getBizCardNo() + "\r\n" +
                    "商户会员卡号： " + cardOpenResponse.getCardInfo().getExternalCardNo() + "\r\n" +
                    "会员卡余额：" + cardOpenResponse.getCardInfo().getBalance() + "\r\n" +
                    "会员卡等级：" + cardOpenResponse.getCardInfo().getLevel() + "\r\n" +
                    "会员卡积分：" + cardOpenResponse.getCardInfo().getPoint() + "\r\n" +
                    "有效期：" + cardOpenResponse.getCardInfo().getValidDate() + "\r\n" +
                    "* 此页面可隐藏 也可作为过度页面使用");

            if (cardOpenResponse.getCardInfo() != null) {
                model.addAttribute("notice", "开卡成功,5秒钟后跳转到会员卡详情页！");
                String alipayCardNo = cardOpenResponse.getCardInfo().getBizCardNo();
                AlipayMarketingCardQueryResponse cardQueryResponse = memberCardService.queryCardByAlipayCardNo(alipayCardNo);
                if (cardQueryResponse != null && cardQueryResponse.getSchemaUrl() != null) {
                    model.addAttribute("redirect_url", cardQueryResponse.getSchemaUrl());
                }
                //开卡数据存储
                MemberCard mc = new MemberCard();
                mc.setExternalCardNo(cardQueryResponse.getCardInfo().getExternalCardNo());
                mc.setAlipayCardNo(cardQueryResponse.getCardInfo().getBizCardNo());
                mc.setCardTemplateId(cardTemplateId);
                mc.setAlipayUserId(tokenResponse.getUserId());
                mc.setSchemaUrl(cardQueryResponse.getSchemaUrl());
//                cardDao.insert(mc);
                logger.info("mc -> {}", mc);
            }
        }

        return "oc_notice";
    }

    //
//    @RequestMapping(value = "/oc_tt.html")
//    public String oc_test(Model model) {
//        model.addAttribute("notice", "success");
//        model.addAttribute("info", "info");
//        model.addAttribute("response",
//                "{\"alipay_marketing_card_open_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"card_info\":{\"balance\":\"10\",\"biz_card_no\":\"test0000004653\",\"external_card_no\":\"1000008765\",\"level\":\"VIP1\",\"open_date\":\"2016-10-19 15:12:07\",\"point\":\"10\",\"valid_date\":\"2017-10-19 15:12:14\"}},\"sign\":\"PkCB+ia3SIoi6q2TTNdeUP3rwCWpM0sWf7oX7vS4OS2xJbYq2h8KBNJD/rcMtJnxfBDkaP28tguV2VKmwmIcEnozRVP1BNMQLPSBJoXmfTe0sv2kZRU3jU7jVaHpXTc7DkT5si91kcSG+TtHmV1Ie5ap+mhLfJKAEnHe+X69Mv4=\"}");
//        return "notice";
//    }
//
//
    private Map<String, Object> getCardTemplateMap() {

        Map<String, Object> templateStyleInfoMap = new HashMap<>();
        templateStyleInfoMap.put("card_show_name", "商户会员卡");//钱包端显示名称（字符串长度）
        templateStyleInfoMap.put("logo_id",
                "Xq1OR03FS-Wxs_LCuUb2OAAAACMAAQED");//logo的图片ID，通过接口（alipay.offline.material.image.upload）上传图片
        templateStyleInfoMap.put("color", "rgb(200,200,200)");//卡片颜色
        templateStyleInfoMap.put("background_id",
                "2HivfguHQEilecaWlPBS4gAAACMAAQED");//背景图片Id，通过接口（alipay.offline.material.image.upload）上传图片
        templateStyleInfoMap.put("bg_color", "rgb(200,200,200)");//背景色

        List<String> featureDescriptionsList = new ArrayList<>();
        featureDescriptionsList.add("XX会员专享");

        templateStyleInfoMap.put("feature_descriptions", featureDescriptionsList);//特色信息，用于领卡预览
        templateStyleInfoMap.put("slogan", "加入会员,永享优惠");//标语
        templateStyleInfoMap.put("slogan_img_id", "1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC");//标语图片， 通过接口（alipay.offline.material.image.upload）上传图片
        templateStyleInfoMap.put("brand_name", "XX品牌");//品牌商名称


        List<String> benefitDescList = new ArrayList<>();
        benefitDescList.add("消费即折扣");

        List<HashMap<String, Object>> templateBenefitInfoList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> templateBenefitInfoMap = new HashMap<>();
        templateBenefitInfoMap.put("title", "消费即折扣");//
        templateBenefitInfoMap.put("benefit_desc", benefitDescList);//

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate = new Date();
        Date endDate = new Date();
        try {
            startDate = dateFormat.parse("2017-02-18 15:17:23");
            endDate = dateFormat.parse("2020-10-18 15:17:23");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        templateBenefitInfoMap.put("start_date", startDate);//
        templateBenefitInfoMap.put("end_date", endDate);//
        templateBenefitInfoList.add(templateBenefitInfoMap);

        HashMap<String, Object> openCardConfMap = new HashMap<>();
        openCardConfMap.put("open_card_source_type", "MER");//ISV：外部系统 MER：直连商户
        openCardConfMap.put("source_app_id", "2017022005782026");//渠道APPID，提供领卡页面的服务提供方  alipay2017022005782026
        openCardConfMap.put("open_card_url", "https://www.alipay.com");//开卡连接，必须http、https开头
        templateBenefitInfoMap.put("conf", "\"\"");//
        templateBenefitInfoList.add(openCardConfMap);


        HashMap<String, Object> moreInfoMap1 = new HashMap<>();
        moreInfoMap1.put("title", "会员链接");
        moreInfoMap1.put("url", "https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.eJ5oJK&treeId=193&articleId=105663&docType=1");

        HashMap<String, Object> moreInfoMap2 = new HashMap<>();
        moreInfoMap2.put("title", "会员专享权益");
        moreInfoMap2.put("params", "{}");
        moreInfoMap2.put("descs", new String[]{"会员生日7折", "会员全单8折", "其他会员专享权益"});

        List<HashMap<String, Object>> columnInfoList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> columnInfoMap1 = new HashMap<>();
        columnInfoMap1.put("code", "LINK");//标准栏位：行为由支付宝统一定，同时已经分配标准Code
        columnInfoMap1.put("more_info", moreInfoMap1);//
        columnInfoMap1.put("title", "会员链接");//栏目的标题
        columnInfoMap1.put("operate_type",
                "openWeb");//1、openNative：打开二级页面，展现 more中descs 2、openWeb：打开URL 3、staticinfo：静态信息
        columnInfoMap1.put("value", "可打开URL");//卡包详情页面，卡栏位右边展现的值

        HashMap<String, Object> columnInfoMap2 = new HashMap<>();
        columnInfoMap2.put("code", "BENEFIT_INFO");//标准栏位：行为由支付宝统一定，同时已经分配标准Code
        columnInfoMap2.put("more_info", moreInfoMap2);//
        columnInfoMap2.put("title", "会员专享");//栏目的标题
        columnInfoMap2.put("operate_type",
                "openNative");//1、openNative：打开二级页面，展现 more中descs 2、openWeb：打开URL 3、staticinfo：静态信息
        columnInfoMap2.put("value", "可打开二级页面");//卡包详情页面，卡栏位右边展现的值


        HashMap<String, Object> columnInfoMap3 = new HashMap<>();
        columnInfoMap3.put("code", "BALANCE");//标准栏位：行为由支付宝统一定，同时已经分配标准Code
        columnInfoMap3.put("title", "余额");//栏目的标题
        columnInfoMap3.put("operate_type",
                "staticinfo");//1、openNative：打开二级页面，展现 more中descs 2、openWeb：打开URL 3、staticinfo：静态信息

        HashMap<String, Object> columnInfoMap4 = new HashMap<>();
        columnInfoMap4.put("code", "POINT");//标准栏位：行为由支付宝统一定，同时已经分配标准Code
        columnInfoMap4.put("title", "积分");//栏目的标题
        columnInfoMap4.put("operate_type",
                "staticinfo");//1、openNative：打开二级页面，展现 more中descs 2、openWeb：打开URL 3、staticinfo：静态信息

        HashMap<String, Object> columnInfoMap5 = new HashMap<>();
        columnInfoMap5.put("code", "LEVEL");//标准栏位：行为由支付宝统一定，同时已经分配标准Code
        columnInfoMap5.put("title", "等级");//栏目的标题
        columnInfoMap5.put("operate_type",
                "staticinfo");//1、openNative：打开二级页面，展现 more中descs 2、openWeb：打开URL 3、staticinfo：静态信息

        columnInfoList.add(columnInfoMap1);
        columnInfoList.add(columnInfoMap2);
        columnInfoList.add(columnInfoMap3);
        columnInfoList.add(columnInfoMap4);
        columnInfoList.add(columnInfoMap5);

        List<HashMap<String, Object>> fieldRuleList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> fieldRuleMap = new HashMap<>();
        fieldRuleMap.put("field_name",
                "Balance");//字段名称，现在支持如下几个Key（暂不支持自定义）Balance：金额 Point：整数 Level：任意字符串 OpenDate：开卡日期 ValidDate：过期日期
        fieldRuleMap.put("rule_name", "ASSIGN_FROM_REQUEST");//
        fieldRuleMap.put("rule_value", "Balance");//
        fieldRuleList.add(fieldRuleMap);

        List<HashMap<String, Object>> pubChannelsList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> pubChannelsMap = new HashMap<>();
        pubChannelsMap.put("pub_channel", "SHOP_DETAIL");//
        pubChannelsMap.put("ext_info", "\"key\":\"value\"");//
        pubChannelsList.add(pubChannelsMap);

        List<HashMap<String, Object>> cardLevleConfList = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> cardLevleConfMap = new HashMap<>();
        cardLevleConfMap.put("level", "VIP1");//
        cardLevleConfMap.put("level_show_name", "黄金会员");//
        cardLevleConfMap.put("level_icon", "1T8Pp00AT7eo9NoAJkMR3AAAACMAAQEC");//
        cardLevleConfMap.put("level_desc", "黄金会员享受免费停车");//
        cardLevleConfList.add(cardLevleConfMap);

        List<String> serviceLabelList = new ArrayList<>();
        serviceLabelList.add("HUABEI_FUWU");

        List<String> shopIdsList = new ArrayList<>();
        shopIdsList.add("2015122900077000000002409504");

        Map<String, Object> map = new HashMap<>();
        map.put("request_id", "332233342233454232341246");//请求ID，由开发者生成并保证唯一性
        map.put("card_type", "OUT_MEMBER_CARD");//卡类型为固定枚举类型，可选类型如下：OUT_MEMBER_CARD：外部权益卡
        map.put("biz_no_prefix", "demo");//业务卡号前缀，由商户自定义

        map.put("biz_no_suffix_len", "10");//
        map.put("write_off_type", "barcode");//
        map.put("template_style_info", templateStyleInfoMap);//模板样式信息
        map.put("column_info_list", columnInfoList);//栏位信息
        map.put("field_rule_list", fieldRuleList);//字段规则列表，会员卡开卡过程中，会员卡信息的生成规则，
//        map.put("template_benefit_info", templateBenefitInfoList);//权益信息 err
//        map.put("open_card_conf", openCardConfMap);
//        map.put("service_label_list", serviceLabelList);
//        map.put("shop_ids", shopIdsList);
//        map.put("pub_channels", pubChannelsList);
//        map.put("card_levle_conf", cardLevleConfList);//err

        return map;
    }

    private Map<String, Object> getCardInfoMap(String userId) {

        Map<String, Object> cardUserInfoMap = new HashMap<>();
        cardUserInfoMap.put("user_uni_id", userId);//
        cardUserInfoMap.put("user_uni_id_type", "UID");//

        //外部卡信息(biz_card_no无需填写)
        Map<String, Object> cardExtInfoMap = new HashMap<>();
//        cardExtInfoMap.put("biz_card_no","");//支付宝生成,返回后保存
        cardExtInfoMap.put("external_card_no", "888888" + RandomUtils.getRandomIdStr(4));//
        cardExtInfoMap.put("open_date", "2017-01-01 15:12:07");//会员卡开卡时间，格式为yyyy-MM-dd HH:mm:ss
        cardExtInfoMap.put("valid_date", "2020-12-31 15:12:14");//会员卡有效期
        cardExtInfoMap.put("level", "VIP8");//会员卡等级（由商户自定义，并可以在卡模板创建时，定义等级信息）
        cardExtInfoMap.put("point", "888");//会员卡积分，积分必须为数字型（可为浮点型，带2位小数点）
        cardExtInfoMap.put("balance", "88.88");//资金卡余额，单位：元，精确到小数点后两位。


        //商户会员信息
        Map<String, Object> memberExtInfoMap = new HashMap<>();
        memberExtInfoMap.put("name", "姓名");//姓名
        memberExtInfoMap.put("gende", "MALE");//性别（男：MALE；女：FEMALE）
        memberExtInfoMap.put("birth", "2016-06-27");//
        memberExtInfoMap.put("cell", "13000000000");//手机号


        Map<String, Object> map = new HashMap<>();
        map.put("out_serial_no", RandomUtils.getRandomRequestId());//外部商户流水号（商户需要确保唯一性控制，类似request_id唯一请求标识）
        map.put("card_user_info", cardUserInfoMap);//发卡用户信息
        map.put("card_ext_info", cardExtInfoMap);//外部卡信息(biz_card_no无需填写)
        map.put("member_ext_info", memberExtInfoMap);//商户会员信息

        return map;
    }

//    private Map<String, Object> getSyncCardMap(){
//
//   	 List<HashMap<String, Object>> useBenefitList = new ArrayList<HashMap<String, Object>>();
//        HashMap<String, Object> useBenefitMap = new HashMap<>();
//        useBenefitMap.put("benefit_type", "COUPON");//
//        useBenefitMap.put("amount", "1");//
//        useBenefitMap.put("description", "消费10元，赠送1元代金券");//
//        useBenefitMap.put("count", "1");//
//        useBenefitList.add(useBenefitMap);
//
//        List<HashMap<String, Object>> gainBenefitList = new ArrayList<HashMap<String, Object>>();
//        HashMap<String, Object> gainBenefitMap = new HashMap<>();
//        gainBenefitMap.put("benefit_type", "COUPON");//
//        gainBenefitMap.put("amount", "1");//
//        gainBenefitMap.put("description", "消费10元，赠送1元代金券");//
//        gainBenefitMap.put("count", "1");//
//        gainBenefitList.add(gainBenefitMap);
//
//
//        HashMap<String, Object> cardInfoMap = new HashMap<>();
////        cardExtInfoMap.put("external_card_no","1000000001");//
//        cardInfoMap.put("open_date","2016-10-19 15:12:07");//会员卡开卡时间，格式为yyyy-MM-dd HH:mm:ss
//        cardInfoMap.put("valid_date","2017-10-19 15:12:14");//会员卡有效期
//        cardInfoMap.put("level","VIP1");//会员卡等级（由商户自定义，并可以在卡模板创建时，定义等级信息）
//        cardInfoMap.put("point","10");//会员卡积分，积分必须为数字型（可为浮点型，带2位小数点）
//        cardInfoMap.put("balance","10");//资金卡余额，单位：元，精确到小数点后两位。
//
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("target_card_no","demo0000000153");//支付宝业务卡号，开卡接口中返回获取
//        map.put("target_card_no_type","BIZ_CARD");//卡号类型BIZ_CARD：支付宝业务卡号
//        map.put("trade_type","TRADE");//交易类型
//        map.put("trade_no","201606270000000001");//支付宝交易号
//        map.put("trade_time","2016-10-19 16:32:14");//线下交易时间
//        map.put("trade_name","消费");
//        map.put("trade_amount","10");
//        map.put("use_benefit_list",useBenefitList);
//        map.put("gain_benefit_list",gainBenefitList);
//        map.put("swipe_cert_type","ALIPAY");
//
//        return map;
//   }
}