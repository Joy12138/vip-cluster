package com.lz.vip.dao

import com.lz.vip.domainmodel.CardTemplate
import com.lz.vip.jooq.tables.records.CardTemplateRecord
import org.jooq.Record
import org.jooq.RecordMapper

@Suppress("UNCHECKED_CAST")
class CardTemplateMapper<R : Record, E : Any> : RecordMapper<R, E> {
    override fun map(r: R): E {
        val record = r.into(CardTemplateRecord::class.java)
        return CardTemplate(
                id = record.id,
                cardTemplateId = record.cardTemplateId
        ) as E
    }
}