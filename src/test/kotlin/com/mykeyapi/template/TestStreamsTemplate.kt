package com.mykeyapi.template

import com.mykeyapi.models.EventTemplateKt.eventCreatedTemplate
import com.mykeyapi.models.EventTemplateOuterClass.EventTemplate
import com.mykeyapi.models.eventTemplate
import com.mykeyapi.template.configuration.AppProperties
import com.mykeyapi.utils.SequenceGenerator
import com.mykeyapi.utils.generateTimestamp
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.TestInputTopic
import org.apache.kafka.streams.TestOutputTopic
import org.apache.kafka.streams.TopologyTestDriver
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.test.context.ActiveProfiles
import java.util.*

@SpringBootTest
@ActiveProfiles("test")
class TestStreamsTemplate {

    @Autowired
    lateinit var appProperties: AppProperties

    @Autowired
    lateinit var sequenceGenerator: SequenceGenerator

    @Autowired
    lateinit var streamsBuilderFactory: StreamsBuilderFactoryBean

//    @Autowired
//    private lateinit var kafkaProperties: KafkaProperties

    @Autowired
    lateinit var stringSerde: Serde<String>

    // Auto-wire serdes here
    @Autowired
    lateinit var inputEventSerde: Serde<EventTemplate>

    @Autowired
    lateinit var outputEventSerde: Serde<EventTemplate>


    lateinit var testDriver: TopologyTestDriver

    // Define topics here
    lateinit var someInputTopic: TestInputTopic<String, EventTemplate>
    lateinit var someOutputTopic: TestOutputTopic<String, EventTemplate>


    @BeforeEach
    fun setup() {
        val properties = Properties()
        properties[StreamsConfig.APPLICATION_ID_CONFIG] = "test"
        properties[StreamsConfig.BOOTSTRAP_SERVERS_CONFIG] = "dummy:1234"

        testDriver = TopologyTestDriver(streamsBuilderFactory.topology, properties)

        // Initialize topics here
        someInputTopic = testDriver.createInputTopic(
            appProperties.someTopic.fullTopicPath,
            stringSerde.serializer(),
            inputEventSerde.serializer()
        )
        someOutputTopic = testDriver.createOutputTopic(
            appProperties.otherTopic.fullTopicPath,
            stringSerde.deserializer(),
            outputEventSerde.deserializer()
        )
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun testStreamTemplate() {
        // Create events
        val event = eventTemplate {
            eventId = sequenceGenerator.nextStringId()
            timestamp = generateTimestamp()
            eventCreated = eventCreatedTemplate {}
        }

        // Pipe events to topics
        someInputTopic.pipeInput(event)

        // Read events from topics
        val outputEvent = someOutputTopic.readKeyValue()

        // Do assertions
        Assertions.assertEquals(event, outputEvent.value)

    }
}

