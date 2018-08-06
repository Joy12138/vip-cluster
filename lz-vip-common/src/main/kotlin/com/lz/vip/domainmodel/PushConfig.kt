package com.lz.vip.domainmodel

import java.io.Serializable

/**
 * 推送列表配置
 */
data class PusherConfig(
        var id: Int? = null,
        var eid: Int? = null,
        var pusherText: String? = null,
        var buttonText: String? = null,
        var picUrl: String? = null,
        var on: Int? = null
) : Serializable {

    companion object {

        private const val serialVersionUID: Long = -1
    }
}

/**
 * 推送事件开启枚举
 */
enum class PusherStats(val stats: Int, val desc: String) {
    OFF(0, "关闭"),
    ON(1, "开启")
}

/**
 * 事件配置
 */
data class EventConfig(
        var eid: Int? = null,
        var name: String? = null,
        var desc: String? = null,
        var param: String? = null,
        var priority: Int? = null
) : Serializable {

    companion object {

        private const val serialVersionUID: Long = -1
    }
}

/**
 * 玩家的事件缓存
 */
data class UserEventCache(
        var uid: String? = null,
        var eid: Int? = null,
        var priority: Int? = null,
        var param: Any? = null
) : Serializable {

    companion object {

        private const val serialVersionUID: Long = -1
    }
}

/**
 * 定时任务锁状态
 */
enum class TaskStatus(val status: Int, val desc: String) {
    LOCK(0, "未获得锁"),
    UNLOCK(1, "获得了锁")
}

/**
 * 手动推送状态
 */
enum class ActivePushStatus(val stats: String, val desc: String) {
    WAIT_FOR_SEND("wait", "待推送"),
    MANUAL_REVOKE("revoke", "已撤销"),
    PROCESS_ON("processing", "进行中"),
    SUCCESS("success", "已完成")
}

/**
 * 手动推送列表配置
 */
data class ManualPushInfo(
        var id: String? = null,
        var createTime: String? = null,
        var pusherText: String? = null,
        var buttonText: String? = null,
        var picUrl: String? = null,
        var startTime: Long? = null,
        var endTime: Long? = null,
        var pushTime: String? = null,
        var count: Float? = null
) : Serializable

/**
 * 手动推送数据返回
 */
data class ManualPushDTO(
        var info: ManualPushInfo? = null,
        var num: Int? = null,
        var stats: String? = null
) : Serializable

/**
 * 定时任务单传递参数
 */
data class RegularJobData(
        var id: String? = null
) : Serializable

/**
 * 推送数据统计
 */
data class PushNum(
        // 三日召回
        var threeDay: Int? = null,
        // 五日召回
        var fiveDay: Int? = null,
        // 七日召回
        var sevenDay: Int? = null
)

/**
 * 推送语言信息
 */
data class PushLocaleInfo(
        // 推送文本
        var pusherText: String = "",
        // 按钮文本
        var buttonText: String = ""
)