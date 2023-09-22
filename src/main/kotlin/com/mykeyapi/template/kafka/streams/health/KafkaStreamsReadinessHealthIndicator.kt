package com.mykeyapi.template.kafka.streams.health

import org.apache.kafka.streams.KafkaStreams
import org.springframework.boot.actuate.availability.ReadinessStateHealthIndicator
import org.springframework.boot.availability.ApplicationAvailability
import org.springframework.boot.availability.AvailabilityState
import org.springframework.boot.availability.ReadinessState
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Component


// Note that class name prefix before `HealthIndicator` will be camel-cased
// and used as a health component name, `kafkaStreams` here
@Component
@Suppress("unused")
class KafkaStreamsReadinessHealthIndicator(
    private val factoryBean: StreamsBuilderFactoryBean,
    availability: ApplicationAvailability

) : ReadinessStateHealthIndicator(availability) {
    //if you have multiple instances, inject as Map<String, KafkaStreams>
    //Spring will map KafkaStreams instances by bean names present in context
    //so you can provide status details for each stream by name


    override fun getState(applicationAvailability: ApplicationAvailability): AvailabilityState? {
        val kafkaStreamsState = factoryBean.kafkaStreams?.state()
        return if (kafkaStreamsState?.equals(KafkaStreams.State.RUNNING) == true)
            ReadinessState.ACCEPTING_TRAFFIC
        else ReadinessState.REFUSING_TRAFFIC


    }


//    override fun health(): Mono<Health> {
//
//        // CREATED, RUNNING or REBALANCING
//        val kafkaStreamsState = factoryBean.kafkaStreams?.state()
//        return (if (kafkaStreamsState?.isRunningOrRebalancing == true) {
//            //set details if you need one
//            Health.up().build()
//        } else Health.down().withDetail("state", kafkaStreamsState?.name).build()).toMono()
//
//        // ERROR, NOT_RUNNING, PENDING_SHUTDOWN,
//    }
}