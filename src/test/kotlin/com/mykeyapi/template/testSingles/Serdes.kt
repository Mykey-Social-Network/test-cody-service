package com.mykeyapi.template.testSingles

import com.mykeyapi.models.EventTemplateOuterClass.EventTemplate
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile


@Profile("test")
@Configuration
@Suppress("unused")
class ProtobufTestSerdes {

    @Bean
    fun eventSerde(): Serde<EventTemplate> =
        Serdes.serdeFrom(
            { _, data -> data?.toByteArray() },
            { _, data -> EventTemplate.parseFrom(data ?: return@serdeFrom null) })


}


