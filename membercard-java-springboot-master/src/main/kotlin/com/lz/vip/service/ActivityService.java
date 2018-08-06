/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.lz.vip.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.CampBaseDto;
import com.alipay.api.domain.CampDetail;
import com.alipay.api.domain.PublishChannel;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.lz.vip.config.AlipayConfig;
import com.lz.vip.utils.RandomUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 口碑活动服务,创建活动及查询服务等
 */
@Component
public class ActivityService {

    private static final Logger logger = LoggerFactory.getLogger(ActivityService.class);
    @Autowired
    private Environment env;

    @Autowired
    private AlipayConfig config;

    private DefaultAlipayClient alipayClient;

    @Autowired
    public void setAlipayClient() {
        this.alipayClient = new DefaultAlipayClient(config.getGateway(), config.getAppid(),
                config.getPrivateKey(), "json", "UTF-8", config.getAlipayPublicKey());
    }

    /**
     * 创建活动
     * @throws AlipayApiException
     */
    public KoubeiMarketingCampaignActivityCreateResponse createActivity(Map<String,Object> map)
            throws AlipayApiException {

        KoubeiMarketingCampaignActivityCreateRequest request = new KoubeiMarketingCampaignActivityCreateRequest();
        //创建活动的异步通知,接收地址
        String notify_url = env.getProperty("alipay.notify.activity");
        request.setNotifyUrl(notify_url);

        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        KoubeiMarketingCampaignActivityCreateResponse response = alipayClient.execute(request);
        logger.info(response.getBody());

        return response;
    }

    /**
     * 修改活动
     * @throws AlipayApiException
     */
    public void modifyActivity(String activityId, Map<String, Object> map) throws AlipayApiException {


        KoubeiMarketingCampaignActivityModifyRequest request = new KoubeiMarketingCampaignActivityModifyRequest();

        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        KoubeiMarketingCampaignActivityModifyResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
    }

    /**
     * 查询单个活动的详情
     * @param campId
     * @return
     * @throws AlipayApiException
     */
    public KoubeiMarketingCampaignActivityQueryResponse queryActivity(String campId)
            throws AlipayApiException {

        Map<String, Object> map = new HashMap<>();
        map.put("camp_id", campId);//

        KoubeiMarketingCampaignActivityQueryRequest request = new KoubeiMarketingCampaignActivityQueryRequest();

        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        KoubeiMarketingCampaignActivityQueryResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
        return response;
    }

    /**
     * 批量查询已发布的活动
     * @return
     * @throws AlipayApiException
     */
    public KoubeiMarketingCampaignActivityBatchqueryResponse batchQueryActivity(Map<String, Object> map)
            throws AlipayApiException {


        KoubeiMarketingCampaignActivityBatchqueryRequest request = new KoubeiMarketingCampaignActivityBatchqueryRequest();

        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        KoubeiMarketingCampaignActivityBatchqueryResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
        return response;
    }

    /**
     * 轮询查询所有活动的详情
     * @throws AlipayApiException
     */
    public void queryAllActivityDetail(Map<String, Object> map) throws AlipayApiException {

        KoubeiMarketingCampaignActivityBatchqueryResponse response = batchQueryActivity(map);
        if (response.isSuccess()) {
            List<CampBaseDto> list = response.getCampSets();
            for (CampBaseDto cb : list) {
                String id = cb.getId();
                queryActivity(id);
            }
        }
    }

    /**
     * 获取发奖的token
     * @return
     * @throws AlipayApiException
     */
    public String prizesendAuth(String prizeId, String userId) throws AlipayApiException {

        Map<String, Object> map = new HashMap<>();
        map.put("req_id", RandomUtils.getRandomRequestId());//
        map.put("prize_id", prizeId);//20160813000000000108127000151337
        map.put("user_id", userId);//2088702052268530

        KoubeiMarketingToolPrizesendAuthRequest request = new KoubeiMarketingToolPrizesendAuthRequest();

        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        KoubeiMarketingToolPrizesendAuthResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
        if (response.isSuccess()) {
            return response.getToken();
        }
        return null;
    }

    /**
     * 获取用户领券的地址,用于授权领券使用
     * @param activityId
     * @param userId
     * @return
     * @throws AlipayApiException
     */
    public String getVoucherUrl(String activityId, String userId) throws AlipayApiException {
        String url = "";
        KoubeiMarketingCampaignActivityQueryResponse response = this
                .queryActivity(activityId);//20161020000000000234584000151419
        if (response.isSuccess() && response.getCampDetail() != null) {
            CampDetail campDetail = response.getCampDetail();

            String token = "";
            PublishChannel pc = campDetail.getPublishChannels().get(0);
            if ("URL_WITH_TOKEN".equalsIgnoreCase(pc.getType())) {
                String extInfo = pc.getExtInfo();
                if (!StringUtils.isEmpty(extInfo)) {
                    JSONObject jsonObject = JSON.parseObject(extInfo);
                    //返回应该这样:https://render.alipay.com/p/z/pointset/index.html?itemId=2016102020076000000009510377
                    url = (String) jsonObject.get("URL_WITH_TOKEN");
                    // 查询活动中的活动奖品ID
                }

                if (!StringUtils.isEmpty(url)) {
                    String itemId = url.substring(url.indexOf("itemId=") + "itemId=".length());
                    if (!StringUtils.isEmpty(itemId)) {
                        //获取发奖的token,  这之前需要商户自己校验用户是否符合发奖规则,如果不符合,可以直接返回给用户不符合信息
                        token = this.prizesendAuth(itemId, userId);
                    }
                }

                if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(token)) {
                    //组装成这样:https://render.alipay.com/p/z/pointset/index.html?type=membercard&itemId=2016091820076000000005569798&token=5b78189e931a415f8c9b9df248a597a9&outBizNo=prize1476966783310
                    url += "&type=membercard&token=" + token + "&outBizNo=" + RandomUtils
                            .getRandomRequestId();

                } else {
                    logger.error("获取领券地址或授权为空");
                }
            }

        }
        //用户使用这个带授权token的url才能去领券
        logger.info("url: " + url);
        return url;
    }
}