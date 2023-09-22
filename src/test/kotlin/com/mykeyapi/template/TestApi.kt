package com.mykeyapi.template


import com.mykeyapi.models.EventTemplateOuterClass.EventTemplate
import com.mykeyapi.models.eventTemplate
import com.mykeyapi.template.api.ControllerTemplate
import com.mykeyapi.template.configuration.AppProperties
import com.mykeyapi.template.testSingles.getJwt
import com.mykeyapi.template.utils.getUser
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.test.context.ActiveProfiles
import reactor.test.StepVerifier
import java.util.concurrent.CompletableFuture

@SpringBootTest(properties = ["spring.rsocket.server.port=0"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class TestApi() {

    @Autowired
    lateinit var appProperties: AppProperties

    @Autowired
    lateinit var controllerTemplate: ControllerTemplate

    @MockBean
    lateinit var eventKafkaTemplate: KafkaTemplate<String, EventTemplate>




    private final val userJwt = getJwt("123456", false)
    val user = userJwt.map(::getUser).block()!!


    @Test
    fun contextLoads() {
    }



    // For this test to work you need to have a running schema registry intense on localhost:8081,
    // and EventTemplate.proto registered to topic "template-service-someTopic-v0"
    @Test
    fun testEndpoint() {

        // Mocking sending to kafka
        val sendResults = SendResult(ProducerRecord("topic", "", eventTemplate {  }), null)

        val kafkaSendResultsFuture =
            CompletableFuture.completedFuture(sendResults)

        Mockito.`when`(
            eventKafkaTemplate.send(
                eq(appProperties.someTopic.fullTopicPath),
                any(String::class.java),
                any(EventTemplate::class.java)
            )
        ).thenReturn(kafkaSendResultsFuture)
        // Get endpoint results
        val endPointResult = controllerTemplate.someEndPoint(userJwt)

        //
        StepVerifier.create(endPointResult)
            .expectNext(true)
            .expectComplete()
            .verify()
    }
}