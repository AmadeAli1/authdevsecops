package com.amade.dev.authdevsecops.configuration

import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator

@Configuration
class DatabaseConfiguration(
) {
    private val logger = java.util.logging.Logger.getLogger(javaClass.simpleName)

    @Bean
    fun initializer(connectionFactory: ConnectionFactory?): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory!!)
        val populate = CompositeDatabasePopulator()
        populate.addPopulators(ResourceDatabasePopulator(ClassPathResource("scheme.sql")))
        initializer.setDatabasePopulator(populate)
        logger.info("Database initialize completed")
        return initializer
    }
}