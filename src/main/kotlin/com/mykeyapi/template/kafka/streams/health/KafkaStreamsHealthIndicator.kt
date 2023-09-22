package com.mykeyapi.template.kafka.streams.health

import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.ReactiveHealthIndicator
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

// Note that class name prefix before `HealthIndicator` will be camel-cased
// and used as a health component name, `kafkaStreams` here
@Component
@Suppress("unused")
class KafkaStreamsHealthIndicator(private val factoryBean: StreamsBuilderFactoryBean) : ReactiveHealthIndicator {
    // If you have multiple instances, inject as Map<String, KafkaStreams>
    // Spring will map KafkaStreams instances by bean names present in context,
    // so you can provide status details for each stream by name

    override fun health(): Mono<Health> {

        // CREATED, RUNNING or REBALANCING
        val kafkaStreamsState = factoryBean.kafkaStreams?.state()
        return (if (kafkaStreamsState?.isRunningOrRebalancing == true) {
            //set details if you need one
            Health.up().build()
        } else Health.down().withDetail("state", kafkaStreamsState?.name).build()).toMono()

        // ERROR, NOT_RUNNING, PENDING_SHUTDOWN,
    }
}