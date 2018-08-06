package com.lz.vip.service.biz

import com.lz.vip.dao.CardTemplateDAO
import com.lz.vip.logger
import com.lz.vip.dao.UserBalanceDAO
import com.lz.vip.domainmodel.CardTemplate
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserBiz {

    private val logger by logger()

    @Autowired private lateinit var balanceDao: UserBalanceDAO

    @Autowired private lateinit var cardTemplateDAO: CardTemplateDAO

    @Autowired private lateinit var dsl: DSLContext

    /**
     * 初始化用户信息
     */
    fun initUser(uid: String) {
        logger.info("服务层开始初始化用户 {} 信息", uid)
        dsl.configuration()
        balanceDao.initUserInfo(uid)
        logger.info("服务层初始化用户信息结束")
    }

    /**
     * 卡模版获取
     */
    fun getLastTemplate(): CardTemplate? {
        return cardTemplateDAO.getLastTemplate()
    }

    fun insertTemplate(templateId: String) {
        return cardTemplateDAO.insertTemplate(templateId)
    }
}