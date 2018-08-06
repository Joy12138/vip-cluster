package com.lz.vip.dao

import com.lz.vip.domainmodel.UserInfo
import com.lz.vip.jooq.tables.records.VipInfoRecord
import org.jooq.Record
import org.jooq.RecordMapper

@Suppress("UNCHECKED_CAST")
class UserInfoMapper<R : Record, E : Any> : RecordMapper<R, E> {
    override fun map(r: R): E {
        val record = r.into(VipInfoRecord::class.java)
        return UserInfo(
                id = record.id,
                userId = record.userId,
                score = record.score,
                createTime = record.createTime,
                updateTime = record.updateTime
        ) as E
    }
}