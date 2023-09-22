package com.mykeyapi.template

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafkaStreams

@EnableKafkaStreams
@SpringBootApplication
@ConfigurationPropertiesScan
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)
}
