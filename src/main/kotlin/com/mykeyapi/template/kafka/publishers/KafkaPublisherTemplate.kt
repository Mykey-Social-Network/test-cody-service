package com.mykeyapi.template.kafka.publishers

import com.mykeyapi.models.EventTemplateOuterClass.EventTemplate
import com.mykeyapi.template.configuration.AppProperties
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class KafkaPublisherTemplate(
    private val appProperties: AppProperties,
    private val inAppEventTemplate: KafkaTemplate<String, EventTemplate>,
) {

    // In order for the publishing to work you need to register the EventTemplate to the topic you want to publish to.
    // This is configured in the gradle in the schema registry plugin configuration.
    fun publishEvent(event: EventTemplate) = Mono.create { sink ->

        val sendRes = inAppEventTemplate.send(appProperties.someTopic.fullTopicPath, event.eventId, event)

        sendRes.whenComplete { _, ex ->
            if (ex != null) {
                sink.success(false)
            } else {
                sink.success(true)
            }
        }
    }
}


