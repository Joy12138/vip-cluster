package com.lz.vip.dao

import com.lz.vip.domainmodel.CardTemplate
import com.lz.vip.jooq.Tables.CARD_TEMPLATE
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
open class CardTemplateDAO {

    @Autowired private lateinit var dsl: DSLContext

    /**
     * 模版卡获取
     */
    fun getLastTemplate(): CardTemplate? {
        TODO()
    }

    /**
     * 模版卡号存入
     */
    fun insertTemplate(templateId: String) {
        dsl.insertInto(CARD_TEMPLATE)
                .set(CARD_TEMPLATE.CARD_TEMPLATE_ID, templateId)
                .execute()
    }
}