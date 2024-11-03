package org.task_manager.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@Suppress("unused")
class JacksonConfiguration {

    @Bean
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerKotlinModule()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
}