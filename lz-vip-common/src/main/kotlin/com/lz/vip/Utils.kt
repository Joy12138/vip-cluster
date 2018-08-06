package com.lz.vip

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import org.redisson.api.LocalCachedMapOptions
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.concurrent.TimeUnit
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType

// 全局变量

// FST 注解
@Target(AnnotationTarget.CLASS)
annotation class FSTShortName(val name: String)

// 序列化函数

/**
 * 将字符串哈希表转化成一个类型为 [M] 的对象。
 */
inline fun <reified M : Any> Map<String, String>.cast(): M {
    val model = try {
        M::class.createInstance()
    } catch (e: Exception) {
        error("无法创建 ${M::class} 的实例，需要默认无参构造函数")
    }
    return cast(model)
}

/**
 * 将字符串哈希表转化成一个类型为 [M] 的对象，并将属性同步到 [destination] 对象中。
 */
fun <M : Any> Map<String, String>.cast(destination: M): M {
    val properties = destination.javaClass.kotlin.memberProperties
    properties.filterIsInstance<KMutableProperty<*>>().forEach {
        val value: String? = this[it.name]
        val data: Any? = when (it.returnType.javaType) {
            Byte::class.javaPrimitiveType,
            Byte::class.javaObjectType -> value?.toByteOrNull()
            Short::class.javaPrimitiveType,
            Short::class.javaObjectType -> value?.toShortOrNull()
            Int::class.javaPrimitiveType,
            Int::class.javaObjectType -> value?.toIntOrNull()
            Long::class.javaPrimitiveType,
            Long::class.javaObjectType -> value?.toLongOrNull()
            Float::class.javaPrimitiveType,
            Float::class.javaObjectType -> value?.toFloatOrNull()
            Double::class.javaPrimitiveType,
            Double::class.javaObjectType -> value?.toDoubleOrNull()
            Boolean::class.javaPrimitiveType,
            Boolean::class.javaObjectType -> value?.toBoolean()
            String::class.java -> value
            else -> null
        }
        it.setter.call(destination, data)
    }
    return destination
}

/**
 * 将一个类型为 [M] 的对象转化成一个字符串哈希表。
 */
fun <M : Any> M.forge(): Map<String, String> {
    val properties = javaClass.kotlin.memberProperties
    return properties
            .associateBy { it.name }
            .mapValues { (_, property) ->
                property.get(this)?.toString()
            }
            .filterValues { it != null }
            .mapValues { it.value!! }
}

// 日志工具函数

/**
 * 懒加载日志器。
 */
fun <T : Any> T.logger(): Lazy<Logger> {
    val enclosingClass: Class<*>? = javaClass.enclosingClass
    // 当该类作为包含类的伴随对象时，始终使用包含类初始化 Logger
    val unwrappedClass: Class<out Any> =
            if (enclosingClass?.kotlin?.companionObject?.java == javaClass) enclosingClass else javaClass
    return lazy {
        LoggerFactory.getLogger(unwrappedClass)
    }
}

// 时间工具函数

/**
 * 表示当前时区下的 Unix 时间戳天数。
 */
internal val localeOffsetDay: Int
    get() {
        val zoneOffset = Calendar.getInstance().get(Calendar.ZONE_OFFSET)
        return TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() + zoneOffset).toInt()
    }

/**
 * 表示当前时区下的 Unix 时间戳月份。
 */
internal val localeOffsetMonth: Int
    get() {
        val month = LocalDate.now().month.value
        val yearOffset = LocalDate.now().year - 1970
        return yearOffset * 12 + month
    }

/**
 * 表示今天的 23:59:59。
 */
internal val todayEnd: Long
    get() = 0.daysLater

/**
 * 表示 N 天后的 23:59:59。
 */
internal val Int.daysLater: Long
    get() = ZonedDateTime.of(LocalDate.now().plusDays(this.toLong()), LocalTime.of(23, 59, 59), ZoneId.systemDefault()).toInstant().epochSecond

/**
 * 表示 N 小时。
 */
val Int.hours: Int
    get() = TimeUnit.HOURS.toSeconds(this.toLong()).toInt()

/**
 * 对列表进行分页查询，查询在第 [page] 页的数据，页面大小为 [pageSize]。
 */
fun <T : Any> List<T>.paging(page: Int, pageSize: Int): List<T> {
    require(page > 0)
    require(pageSize > 0)
    return asSequence().drop((page - 1) * pageSize).take(pageSize).toList()
}

// 测试工具函数

/**
 * 构造一个用于 JUnit 参数化测试的单位集合，内容为一个长度为 [count] 的数组，内容为一个生成参数 [P] 的 `lambda` 构成的单位数组。
 */
inline fun <reified P : Any> parameters(count: Int = 1, block: () -> P): Collection<Array<*>> {
    return listOf(*Array(count) { arrayOf(block()) })
}

internal inline fun <K : Any, V : Any> cache(crossinline loader: (K) -> V): LoadingCache<K, V> {
    return CacheBuilder.newBuilder()
            .initialCapacity(1024)
            .maximumSize(4096)
            .expireAfterWrite(2, TimeUnit.HOURS)
            .build(object : CacheLoader<K, V>() {

                override fun load(key: K): V {

                    return loader(key)
                }
            })
}

internal fun <K : Any, V : Any> staticCache(ttl: Int = 3): LocalCachedMapOptions<K, V> {
    val time = ttl.coerceAtLeast(1).toLong()
    return LocalCachedMapOptions
            .defaults<K, V>()
            .syncStrategy(LocalCachedMapOptions.SyncStrategy.UPDATE)
            .timeToLive(time, TimeUnit.SECONDS)
            .maxIdle(time, TimeUnit.SECONDS)
}

fun ObjectMapper.isValidJSON(input: String): Boolean {
    return try {
        readTree(input)
        true
    } catch (e: Exception) {
        false
    }
}
