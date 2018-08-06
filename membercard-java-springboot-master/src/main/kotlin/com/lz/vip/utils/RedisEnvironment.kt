package com.lz.vip.utils

import com.lz.vip.FSTShortName
import org.nustaq.serialization.FSTBasicObjectSerializer
import org.nustaq.serialization.FSTClazzInfo
import org.nustaq.serialization.FSTConfiguration
import org.nustaq.serialization.FSTObjectInput
import org.nustaq.serialization.FSTObjectOutput
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.codec.FstCodec
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import java.util.Locale
import kotlin.reflect.KClass

@Configuration
@PropertySource("classpath:application-dev.yml")
open class RedisEnvironment {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Bean
    open fun redis(fstConfiguration: FSTConfiguration, @Value("\${redis.lz_vip}") redisString: String): RedissonClient {
        val array = redisString.split(":")
        return Redisson.create(Config()
                .apply {
                    useSingleServer()
                            .setAddress("redis://${array[0]}:${array[1]}")
                            .password = if (array.size >= 3) array[2] else null
                    this.codec = FstCodec(fstConfiguration)
                })
    }

    @Bean
    open fun stats(fstConfiguration: FSTConfiguration, @Value("\${redis.lz_vip}") redisString: String): RedissonClient {
        val array = redisString.split(":")
        return Redisson.create(Config()
                .apply {
                    useSingleServer()
                            .setDatabase(1)
                            .setAddress("redis://${array[0]}:${array[1]}")
                            .password = if (array.size >= 3) array[2] else null
                    this.codec = FstCodec(fstConfiguration)
                })
    }

    @Bean
    open fun fstConfiguration(): FSTConfiguration {
        return FSTConfiguration.createJsonNoRefConfiguration().apply {
            registerSerializer(Locale::class.java, FSTLocaleSerializer, false)
            val beans: Collection<Any> = applicationContext.getBeansWithAnnotation(FSTShortName::class.java).values
//            register("chapter", Chapter::class)
        }
    }
}

private fun <T : Any> FSTConfiguration.register(shortName: String, kClass: KClass<T>) {
    registerCrossPlatformClassMapping(shortName, kClass.java.name)
}

private object FSTLocaleSerializer : FSTBasicObjectSerializer() {

    override fun writeObject(out: FSTObjectOutput, toWrite: Any, clzInfo: FSTClazzInfo, referencedBy: FSTClazzInfo.FSTFieldInfo, streamPosition: Int) {
        out.writeStringUTF((toWrite as Locale).language)
    }

    override fun instantiate(objectClass: Class<*>, `in`: FSTObjectInput, serializationInfo: FSTClazzInfo, referencee: FSTClazzInfo.FSTFieldInfo, streamPosition: Int): Any {
        val res: Locale = Locale.Builder().setLanguage(`in`.readStringUTF()).build()
        `in`.registerObject(res, streamPosition, serializationInfo, referencee)
        return res
    }
}
