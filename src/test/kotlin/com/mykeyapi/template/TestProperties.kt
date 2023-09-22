package com.mykeyapi.template

import com.mykeyapi.template.configuration.AppProperties
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("test")
class TestProperties {
    @Autowired
    lateinit var appProperties: AppProperties

    @Test
    fun contextLoads() {
    }

    @Test
    fun testTopicNames() {
        Assertions.assertEquals("someService-someTopic-v0", appProperties.someTopic.fullTopicPath)
    }
}