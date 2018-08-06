package com.lz.vip.dao

import com.lz.vip.domainmodel.CardTemplate
import com.lz.vip.domainmodel.UserInfo
import org.jooq.Record
import org.jooq.RecordMapper
import org.jooq.RecordMapperProvider
import org.jooq.RecordType
import org.jooq.impl.DefaultRecordMapper

class MapperProvider : RecordMapperProvider {
    override fun <R : Record, E : Any> provide(type: RecordType<R>, clazz: Class<out E>): RecordMapper<R, E> {
        return when (clazz) {

            UserInfo::class.java -> UserInfoMapper()

            CardTemplate::class.java -> CardTemplateMapper()

            else -> DefaultRecordMapper(type, clazz)
        }
    }
}