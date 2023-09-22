package com.mykeyapi.template.kafka.streams

import com.mykeyapi.models.EventTemplateOuterClass.EventTemplate
import com.mykeyapi.template.configuration.AppProperties
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
@Suppress("unused")
class StreamTemplate(
    private val appProperties: AppProperties, private val streamsBuilder: StreamsBuilder,
    private val stringSerde: Serde<String>, private val eventSerde: Serde<EventTemplate>,
) {

    @Autowired
    fun streamExample() {
        streamsBuilder.stream(appProperties.someTopic.fullTopicPath, Consumed.with(stringSerde, eventSerde))
            .to(appProperties.otherTopic.fullTopicPath)
    }
}
