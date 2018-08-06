/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.lz.vip.service;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayOfflineMarketShopBatchqueryRequest;
import com.alipay.api.request.AlipayOfflineMarketShopQuerydetailRequest;
import com.alipay.api.response.AlipayOfflineMarketShopBatchqueryResponse;
import com.alipay.api.response.AlipayOfflineMarketShopQuerydetailResponse;
import com.lz.vip.config.AlipayConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 口碑活动服务,创建活动及查询服务等
 */
@Component
public class ShopService {

    private static final Logger logger = LoggerFactory.getLogger(ShopService.class);

    @Autowired
    private AlipayConfig config;

    private DefaultAlipayClient alipayClient;

    @Autowired
    public void setAlipayClient() {
        this.alipayClient = new DefaultAlipayClient(config.getGateway(), config.getAppid(),
                config.getPrivateKey(), "json", "UTF-8", config.getAlipayPublicKey());
    }

    /**
     * 轮询查询各门店的详情
     * @throws AlipayApiException
     */
    public void queryAllShopDetail() throws AlipayApiException {
        AlipayOfflineMarketShopBatchqueryResponse response = this.batchQueryShop();
        List<String> list = response.getShopIds();
        for (String shopId : list) {
            queryShop(shopId);
        }
    }

    /**
     * 查询门店详情
     * @param shopId
     * @throws AlipayApiException
     */
    public void queryShop(String shopId) throws AlipayApiException {
        Map<String, Object> map = new HashMap<>();
        map.put("shop_id", shopId);//
        map.put("op_role", "MERCHANT");

        AlipayOfflineMarketShopQuerydetailRequest request = new AlipayOfflineMarketShopQuerydetailRequest();

        request.setBizContent(JSON.toJSONString(map));

        logger.info(request.getBizContent());

        AlipayOfflineMarketShopQuerydetailResponse response = alipayClient.execute(request);
        logger.info(response.getBody());
    }

    /**
     * 批量查询门店
     * @return
     * @throws AlipayApiException
     */
    public AlipayOfflineMarketShopBatchqueryResponse batchQueryShop() throws AlipayApiException {
        AlipayOfflineMarketShopBatchqueryRequest request = new AlipayOfflineMarketShopBatchqueryRequest();
        request.setBizContent("{" +
                              "    \"page_no\":\"1\"" +
                              "  }");
        AlipayOfflineMarketShopBatchqueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
        logger.info(response.getBody());
        return response;
    }

}