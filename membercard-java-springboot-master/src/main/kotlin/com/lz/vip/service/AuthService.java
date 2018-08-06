/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.lz.vip.service;

import com.alibaba.fastjson.JSONArray;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayPlatformUseridGetRequest;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.response.AlipayPlatformUseridGetResponse;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.lz.vip.config.AlipayConfig;
import com.lz.vip.enums.AuthScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *
 */
@Component
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private AlipayConfig config;

    private DefaultAlipayClient alipayClient;

    @Autowired
    private Environment env;

    @Autowired
    public void setAlipayClient() {
        this.alipayClient = new DefaultAlipayClient(config.getGateway(), config.getAppid(),
                config.getPrivateKey(), "json", "UTF-8", config.getAlipayPublicKey());
    }

    /**
     * 根据授权code获取userId
     *
     * @param authCode
     * @return
     * @throws AlipayApiException
     */
    public String getUserId(String authCode) throws AlipayApiException {

        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(authCode);

        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            return response.getUserId();
        }
        return null;
    }

    /**
     * 根据OpenId获取UserId
     *
     * @return
     */
    public String getUserIdByOpenId(String openId) throws AlipayApiException {

        AlipayPlatformUseridGetRequest request = new AlipayPlatformUseridGetRequest();
        request.setBizContent("{" +
                "      \"open_ids\":[" +
                openId +
                "      \"]" +
                "  }");


        logger.info(request.getBizContent());
        AlipayPlatformUseridGetResponse response = alipayClient.execute(request);

        logger.info(response.getBody());
        if (response.isSuccess()) {
            String dict = response.getDict();
            JSONArray ja = JSONArray.parseArray(dict);
            if (ja != null && ja.size() > 0) {
                return ja.get(0).toString();
            }
        }

        return null;
    }

    /**
     * 根据授权code获取token及uid返回
     *
     * @param authCode
     * @return
     * @throws AlipayApiException
     */
    public AlipaySystemOauthTokenResponse getTokenResponse(String authCode)
            throws AlipayApiException {
        AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
        request.setGrantType("authorization_code");
        request.setCode(authCode);

        AlipaySystemOauthTokenResponse response = alipayClient.execute(request);
        return response;
    }

    /**
     * 获取拼接的授权地址
     *
     * @param returnUrl
     * @param scope
     * @return
     */
    public String getAuthUrl(String returnUrl, AuthScope scope1, AuthScope scope2) {

        logger.info("returnUrl: " + returnUrl);
        String authGateway = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm";
        if (env.getProperty("alipay.config.auth_gateway") != null) {
            authGateway = env.getProperty("alipay.config.auth_gateway");
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(authGateway);
            sb.append("?app_id=");
            sb.append(config.getAppid());
            sb.append("&scope=");
            sb.append(scope1.toString().toLowerCase());
            if (scope2 != null && scope2.toString().length() > 0)
                sb.append("," + scope2.toString().toLowerCase());
            sb.append("&redirect_uri=");
            sb.append(URLEncoder.encode(returnUrl, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logger.info("authUrl: " + sb.toString());
        return sb.toString();
    }
}