/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.lz.vip.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.lz.vip.config.AlipayConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 *
 */

@Component
public class MemberCardService {

    private static final Logger logger = LoggerFactory.getLogger(MemberCardService.class);

    @Autowired
    private AlipayConfig config;

    private DefaultAlipayClient alipayClient;

    @Autowired
    public void setAlipayClient(){
        this.alipayClient = new DefaultAlipayClient(config.getGateway(),
                config.getAppid(), config.getPrivateKey(), "json", "UTF-8",
                config.getAlipayPublicKey());
    }

    /**
     * 上传素材
     */
    public void uploadImage() throws AlipayApiException {


        AlipayOfflineMaterialImageUploadRequest request = new AlipayOfflineMaterialImageUploadRequest();
        request.setImageType("jpg");
        request.setImageName("logo");
        request.setImagePid(config.getPid());

        FileItem ImageContent = new FileItem("");
        request.setImageContent(ImageContent);


        AlipayOfflineMaterialImageUploadResponse response = alipayClient.execute(request);
        logger.info(response.getBody());

    }
    
    /**
     * 获取用户信息
     * @param authToken
     * @return AlipayUserInfoShareResponse
     * @throws AlipayApiException
     */
    public AlipayUserInfoShareResponse getUserInfo(String authToken) throws AlipayApiException {
    	
    	AlipayUserInfoShareRequest request = new AlipayUserInfoShareRequest();
        	
    	AlipayUserInfoShareResponse userinfoShareResponse = null;
    	try {
    		userinfoShareResponse = alipayClient.execute(request, authToken);
    	    System.out.println(userinfoShareResponse.getBody());
    	} catch (AlipayApiException e) {
    	    e.printStackTrace();
    	}
    	
    	return userinfoShareResponse;
    }

    public AlipayMarketingCardTemplateCreateResponse createCardTemplate(Map<String, Object> map) throws AlipayApiException {
        AlipayMarketingCardTemplateCreateRequest request = new AlipayMarketingCardTemplateCreateRequest();
        request.setBizContent(JSON.toJSONString(map));
        logger.info(request.getBizContent());
        AlipayMarketingCardTemplateCreateResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
        return response;
    }

    public AlipayMarketingCardTemplateModifyResponse modifyCardTemplate(String templateId, Map<String, Object> map) throws AlipayApiException {

    	 AlipayMarketingCardTemplateModifyRequest request = new AlipayMarketingCardTemplateModifyRequest();
         
    	 map.put("template_id", "");//卡模版ID
         request.setBizContent(JSON.toJSONString(map));

         logger.info(request.getBizContent());

         AlipayMarketingCardTemplateModifyResponse response = alipayClient.execute(request);
         logger.info(response.getBody());
         return response;
         
    }

    public void queryCardTemplate(String templateId) throws AlipayApiException {

        AlipayMarketingCardTemplateQueryRequest request = new AlipayMarketingCardTemplateQueryRequest();
        request.setBizContent("{\"template_id\":\""+templateId+"\" }");
        AlipayMarketingCardTemplateQueryResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
    }

    public AlipayMarketingCardOpenResponse openCard(String cardTemplateId, Map<String, Object> map, String accessToken) throws AlipayApiException {

    	map.put("card_template_id",cardTemplateId);//支付宝分配的卡模板Id（卡模板创建接口返回的模板ID）

        AlipayMarketingCardOpenRequest request = new AlipayMarketingCardOpenRequest();

        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        AlipayMarketingCardOpenResponse response = alipayClient.execute(request,accessToken);
        logger.info(response.getBody());
        return response;
    }

    public void updateCard() {

    }

    public void deleteCard() {

    }

    /**
     * 根据支付宝卡号查询会员卡信息
     * @param alipayCardNo
     * @throws AlipayApiException
     */
    public AlipayMarketingCardQueryResponse queryCardByAlipayCardNo(String alipayCardNo) throws AlipayApiException {
        Map<String, Object> map = new HashMap<>();
        map.put("target_card_no",alipayCardNo);//支付宝业务卡号，开卡接口中返回获取  demo0000000153
        map.put("target_card_no_type","BIZ_CARD");//卡号类型BIZ_CARD：支付宝业务卡号

        AlipayMarketingCardQueryRequest request = new AlipayMarketingCardQueryRequest();
        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        AlipayMarketingCardQueryResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
        return response;
    }

    /**
     * 根据会员卡的动态条码查询卡
     * @param cardDBarCode
     * @throws AlipayApiException
     */
    public void queryCardByBarCode(String cardDBarCode) throws AlipayApiException {
        Map<String, Object> map = new HashMap<>();
        map.put("target_card_no",cardDBarCode);//支付宝业务卡号，开卡接口中返回获取
        map.put("target_card_no_type","D_BAR_CODE");//D_BAR_CODE：动态条码

        AlipayMarketingCardQueryRequest request = new AlipayMarketingCardQueryRequest();
        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        AlipayMarketingCardQueryResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
    }

    
    public void syncCard(Map<String, Object> map) throws AlipayApiException {

        AlipayMarketingCardConsumeSyncRequest request = new AlipayMarketingCardConsumeSyncRequest();
        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        AlipayMarketingCardConsumeSyncResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
    }
}