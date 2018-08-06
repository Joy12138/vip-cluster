//package com.lz.vip.dao
//
//import org.redisson.api.LocalCachedMapOptions
//import org.redisson.api.RAtomicLong
//import org.redisson.api.RBucket
//import org.redisson.api.RLocalCachedMap
//import org.redisson.api.RedissonClient
//import java.util.concurrent.TimeUnit
//import javax.annotation.PostConstruct
//import com.google.common.cache.LoadingCache
//import cache
//import ActivePushStatus
//import ManualPushInfo
//import staticCache
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.stereotype.Repository
//
//@Repository
//class FbActivePushDAO {
//
//    @Value("\${chalk.gameId:1097}")
//    internal lateinit var gameId: String
//
//    @Autowired
//    internal lateinit var redis: RedissonClient
//
//    private lateinit var activeIdCounter: RAtomicLong
//
//    private lateinit var activePushInfo: RLocalCachedMap<String, ManualPushInfo>
//
//    /**
//     * 手动推送状态
//     */
//    private lateinit var activeStats: LoadingCache<String, RBucket<String>>
//
//    /**
//     * 比率推送列表映射
//     */
//    private lateinit var mapperSet: LoadingCache<String, RBucket<ArrayList<String>>>
//
//    /**
//     * 手动推送数量
//     */
//    private lateinit var pushCount: LoadingCache<String, RBucket<Int>>
//
//    /**
//     * 手动推送记录列表
//     */
//    private val activePushOptions: LocalCachedMapOptions<String, ManualPushInfo> = staticCache()
//
//    @PostConstruct
//    fun setUp() {
//        activeIdCounter = redis.getAtomicLong("kk:game-pay:$gameId:facebook:active:push:id-gen")
//
//        activePushInfo = redis.getLocalCachedMap("kk:game-pay:$gameId:facebook:active:push", activePushOptions)
//        /**
//         * 手动推送状态
//         */
//        activeStats = cache { redis.getBucket("kk:game-pay:$gameId:facebook:active:push:stats:$it") }
//
//        /**
//         * 推送id映射列表
//         */
//        mapperSet = cache { redis.getBucket("kk:game-pay:$gameId:facebook:active:mapper:push:stats:$it") }
//
//        /**
//         * 手动推送数量
//         */
//        pushCount = cache { redis.getBucket("kk:game-pay:$gameId:facebook:active:push:count:$it") }
//    }
//
//    /**
//     * 创建任务单记录
//     */
//    fun setRegularTask(config: ManualPushInfo): String {
//        val id = config.id ?: activeIdCounter.incrementAndGet().toString()
//        config.id = id
//        activePushInfo.fastPut(id, config)
//        return id
//    }
//
//    /**
//     * 查询是否存在某个任务单
//     */
//    fun taskExist(id: String): Boolean {
//        return activePushInfo.contains(id)
//    }
//
//    /**
//     * 获取任务单记录
//     */
//    fun getTask(id: String): ManualPushInfo? {
//        return activePushInfo[id]
//    }
//
//    /**
//     * 删除任务单记录
//     */
//    fun delTask(id: String) {
//        activePushInfo.remove(id)
//    }
//
//    /**
//     * 获取全部任务单记录
//     */
//    fun getAllTasks(): Collection<ManualPushInfo> {
//        return ArrayList(activePushInfo.values)
//    }
//
//    /**
//     * 变更手动推送状态
//     */
//    fun changeStats(id: String, stats: ActivePushStatus) {
//        val bucket = activeStats[id]
//        bucket.set(stats.stats)
//    }
//
//    /**
//     * 查询手动推送状态
//     */
//    fun getStats(id: String): String? {
//        val bucket = activeStats[id]
//        return bucket.get()
//    }
//
//    /**
//     * 查询统计数据
//     */
//    fun getStatisticData(id: String): Int? {
//        val bucket = pushCount[id]
//        return bucket.get()
//    }
//
//    /**
//     * 保存统计数据
//     */
//    fun saveStatisticData(id: String, item: Int) {
//        val bucket = pushCount[id]
//        bucket.set(item)
//    }
//
//    /**
//     * 创建 [psids] 列表的映射
//     */
//    fun createMapper(id: String, list: ArrayList<String>) {
//        val bucket = mapperSet[id]
//        bucket.set(list)
//        bucket.expire(2, TimeUnit.DAYS)
//    }
//
//    /**
//     * 根据 [id] 获取 [psids] 映射列表
//     */
//    fun getMapper(id: String): ArrayList<String> {
//        val bucket = mapperSet[id] ?: return arrayListOf()
//        return bucket.get()
//    }
//}