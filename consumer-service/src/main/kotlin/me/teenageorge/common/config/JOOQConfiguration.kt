package me.teenageorge.common.config

import io.r2dbc.spi.ConnectionFactory
import org.jooq.impl.DSL
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.r2dbc.connection.R2dbcTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@Configuration
class JOOQConfiguration {

    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory) = R2dbcTransactionManager(connectionFactory)

    @Bean
    fun dslContext(connectionFactory: ConnectionFactory) = DSL.using(connectionFactory)
}
