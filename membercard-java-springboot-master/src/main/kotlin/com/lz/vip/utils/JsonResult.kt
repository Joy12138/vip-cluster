package com.lz.vip.utils

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonValue
import com.lz.vip.utils.ServerReply.SYSTEM_OK

/**
 * 返回结果
 */
data class JSONResult @JsonCreator constructor(
        val code: Int = SYSTEM_OK.code,
        val msg: String = SYSTEM_OK.message,
        @JsonInclude(JsonInclude.Include.NON_NULL) var data: Any? = null
)

internal val JSONResult.isOkay: Boolean
    get() = code == SYSTEM_OK.code

internal fun okay(block: ((result: JSONResult) -> Unit)? = null): JSONResult {
    return statusOf(SYSTEM_OK, block)
}

internal fun statusOf(adminServerReply: ServerReply, block: ((result: JSONResult) -> Unit)? = null): JSONResult {
    return JSONResult(adminServerReply.code, adminServerReply.message).apply {
        block?.invoke(this)
    }
}

internal fun okay(collection: Collection<*>): JSONResult {
    return statusOf(SYSTEM_OK, {
        it.data = mapOf(
                "info" to collection,
                "count" to collection.size
        )
    })
}

enum class ServerReply(val code: Int, val message: String) {

    SYSTEM_OK(200, "OK"),
    BAD_REQUEST(400, "非法请求"),
    ERROR_PARAM(1001, "非法参数"),
    ;

    @JsonValue
    fun getStatus(): JSONResult = statusOf(this)
}
