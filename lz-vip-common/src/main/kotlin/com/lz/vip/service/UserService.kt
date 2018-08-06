package com.lz.vip.service

import com.lz.vip.domainmodel.CardTemplate

/**
 * 用户信息相关
 */
interface UserService {

    /**
     * 初始化用户数据
     */
    fun initUserStaticInfo(uid: String)

    /**
     * 卡模版获取
     */
    fun getLastTemplate(): CardTemplate?

    /**
     * 卡模版存入
     */
    fun insertTemplate(templateId: String)
}