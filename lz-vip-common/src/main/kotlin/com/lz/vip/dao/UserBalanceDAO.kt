package com.lz.vip.dao

import com.lz.vip.jooq.Tables.VIP_INFO
import com.lz.vip.jooq.tables.records.VipInfoRecord
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.sql.Timestamp

@Component
open class UserBalanceDAO {

    @Autowired private lateinit var dsl: DSLContext

    fun initUserInfo(uid: String): Int {
        val now = Timestamp(System.currentTimeMillis())
        return dsl.insertInto<VipInfoRecord>(VIP_INFO)
                .set(VIP_INFO.USER_ID, uid)
                .set(VIP_INFO.SCORE, 0L)
                .set(VIP_INFO.UPDATE_TIME, now)
                .set(VIP_INFO.CREATE_TIME, now)
                .onDuplicateKeyIgnore()
                .execute()
    }
}

