package com.lz.vip.service

import com.lz.vip.domainmodel.CardTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import com.lz.vip.service.biz.UserBiz

@Service
class UserServiceImpl: UserService {

    @Autowired private lateinit var userBiz: UserBiz

    /**
     * 初始化用户基础数据
     */
    override fun initUserStaticInfo(uid: String) {
        userBiz.initUser(uid)
    }

    /**
     * 卡模版获取
     */
    override fun getLastTemplate(): CardTemplate? {
        return userBiz.getLastTemplate()
    }

    /**
     * 卡模版存入
     */
    override fun insertTemplate(templateId: String) {
        userBiz.insertTemplate(templateId)
    }
}