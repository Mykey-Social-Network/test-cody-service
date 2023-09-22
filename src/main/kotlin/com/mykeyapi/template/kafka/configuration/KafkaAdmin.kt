package com.mykeyapi.template.kafka.configuration

import com.mykeyapi.template.configuration.AppProperties
import org.apache.kafka.clients.admin.NewTopic
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder



@Configuration
@Suppress("unused")
class KafkaTopicsCreator(private val appProperties: AppProperties) {

    private val logger = LoggerFactory.getLogger(KafkaTopicsCreator::class.java)

    fun buildTopic(topic: AppProperties.Topic): NewTopic {
        logger.info("Creating topic: ${topic.fullTopicPath}")
        return TopicBuilder.name(topic.fullTopicPath)
            .replicas(appProperties.replications.toInt())
            .partitions(topic.partitions ?: appProperties.partitions.toInt())
            .let {
                if (topic.isCompact) it.compact()
                else it
            }
            .let {
                if (topic.retentionBytes != null)
                    it.config("retention.bytes", topic.retentionBytes.toString())
                else it
            }
            .let {
                if (topic.retentionMs != null)
                    it.config("retention.ms", topic.retentionMs.toString())
                else it
            }
            .build()
    }


    // Topic builder beans
    @Bean
    fun someTopic(): NewTopic = buildTopic(appProperties.someTopic)
}
