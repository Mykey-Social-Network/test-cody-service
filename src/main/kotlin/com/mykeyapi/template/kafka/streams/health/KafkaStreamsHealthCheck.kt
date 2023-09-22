package com.mykeyapi.template.kafka.streams.health

import org.apache.kafka.streams.KafkaStreams
import org.springframework.boot.actuate.health.Health
import org.springframework.boot.actuate.health.ReactiveHealthIndicator
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class KafkaStreamsHealthCheck(private val streamsBuilderFactoryBean: StreamsBuilderFactoryBean) : ReactiveHealthIndicator  {
    override fun health(): Mono<Health> {
        val kafkaStreams: KafkaStreams = streamsBuilderFactoryBean.kafkaStreams!!
        val state: KafkaStreams.State = kafkaStreams.state()

        return if (KafkaStreams.State.ERROR == state) {
            Mono.just(Health.down().withDetail("Error", "Kafka Streams state is $state").build())
        } else {
            Mono.just(Health.up().build())
        }
    }
}