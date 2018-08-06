package com.lz.vip.domainmodel

import java.sql.Timestamp

data class UserInfo(
        // 主键id
        var id: Int? = null,
        // 用户id
        var userId: String? = null,
        // 用户积分
        var score: Long? = null,
        // 创建时间
        var createTime: Timestamp? = null,
        // 更新时间
        var updateTime: Timestamp? = null
)

/**
 * 卡模版
 */
data class CardTemplate(
        // 主键id
        var id: Int? = null,
        // 模版卡卡号
        var cardTemplateId: String? = null
)

enum class isNew(val code: Int, val desc: String) {
    NEW(0, "新用户"),
    OLD(1, "旧用户")
}
