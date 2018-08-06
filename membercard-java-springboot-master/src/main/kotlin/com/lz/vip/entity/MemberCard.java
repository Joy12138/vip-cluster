/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2016 All Rights Reserved.
 */
package com.lz.vip.entity;


/**
 *
 */
public class MemberCard {

    private Integer id;
    private String alipayCardNo;
    private String externalCardNo;
    private String alipayUserId;
    private String cardTemplateId;
    private String schemaUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlipayCardNo() {
        return alipayCardNo;
    }

    public void setAlipayCardNo(String alipayCardNo) {
        this.alipayCardNo = alipayCardNo;
    }

    public String getExternalCardNo() {
        return externalCardNo;
    }

    public void setExternalCardNo(String externalCardNo) {
        this.externalCardNo = externalCardNo;
    }

    public String getAlipayUserId() {
        return alipayUserId;
    }

    public void setAlipayUserId(String alipayUserId) {
        this.alipayUserId = alipayUserId;
    }

    public String getCardTemplateId() {
        return cardTemplateId;
    }

    public void setCardTemplateId(String cardTemplateId) {
        this.cardTemplateId = cardTemplateId;
    }

    public String getSchemaUrl() {
        return schemaUrl;
    }

    public void setSchemaUrl(String schemaUrl) {
        this.schemaUrl = schemaUrl;
    }
}