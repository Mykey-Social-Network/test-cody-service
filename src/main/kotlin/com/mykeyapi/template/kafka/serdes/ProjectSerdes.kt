package com.mykeyapi.template.kafka.serdes

import com.mykeyapi.models.EventTemplateOuterClass.EventTemplate
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig
import io.confluent.kafka.streams.serdes.protobuf.KafkaProtobufSerde
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes.StringSerde
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile


@Configuration
@Suppress("unused")
class ProjectSerdes {
    @Bean
    fun stringSerde(): Serde<String> = StringSerde()
}


@Configuration
@Suppress("unused")
@Profile("!test")
class ProtobufSerdes {
    @Value("\${spring.kafka.properties.schema.registry.url}")
    private lateinit var schemaRegistryUrl: String

    @Value("\${spring.kafka.properties.basic.auth.user.info}")
    private lateinit var schemaCred: String

    @Value("\${spring.kafka.properties.ssl.truststore.location:#{null}}")
    private var schemaSSLLocation: String? = null

    @Value("\${spring.kafka.properties.ssl.truststore.password:#{null}}")
    private var schemaSSLPassword: String? = null



    val serdeConfiguration: () -> MutableMap<String, String> = {
        mutableMapOf(
            AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG to schemaRegistryUrl,
            AbstractKafkaSchemaSerDeConfig.BASIC_AUTH_CREDENTIALS_SOURCE to "USER_INFO",
            AbstractKafkaSchemaSerDeConfig.USER_INFO_CONFIG to schemaCred,

            ).also { map ->
            schemaSSLLocation?.let {
                map.put("schema.registry.ssl.truststore.location", it)
            }
            schemaSSLPassword?.let {
                map.put("schema.registry.ssl.truststore.password", it)
            }
        }
    }





    @Bean
    fun eventSerde(): Serde<EventTemplate> =
        KafkaProtobufSerde(EventTemplate::class.java).apply {
            configure(serdeConfiguration(), false)
        }

}


