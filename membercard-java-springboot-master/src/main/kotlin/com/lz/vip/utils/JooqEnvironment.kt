package com.lz.vip.utils

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jooq.impl.DataSourceConnectionProvider
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import java.util.Properties

@Configuration
@PropertySource("classpath:application-dev.yml")
open class JooqEnvironment {

    open fun createDsl(): DefaultDSLContext {
        return DefaultDSLContext(
                DefaultConfiguration().apply {
                    DataSourceConnectionProvider(
                            TransactionAwareDataSourceProxy(
                                    HikariDataSource(
                                            HikariConfig().apply {
                                                poolName = pName
                                                dataSourceClassName = sourceName
                                                dataSourceProperties = Properties().apply {
                                                    setProperty("url", url)
                                                    setProperty("user", username)
                                                    setProperty("password", password)
                                                }
                                            }
                                    )
                            )
                    )
                }
        )
    }

    companion object {

        @Value("\${spring.datasource.hikari.poolName}")
        private var pName: String? = null

        @Value("\${spring.datasource.hikari.dataSourceClassName}")
        private var sourceName: String? = null

        @Value("\${spring.datasource.url}")
        private var url: String? = null

        @Value("\${spring.datasource.username}")
        private var username: String? = null

        @Value("\${spring.datasource.password}")
        private var password: String? = null
    }
}