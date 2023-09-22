package com.mykeyapi.template.configuration

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "app-properties")
class AppProperties(
    var topics: Topics,
    var stores: Stores,
    val partitions: String,
    val replications: String,
    var useInMemoryStore: Boolean,
) {
    companion object{
        private  val log = LoggerFactory.getLogger(AppProperties::class.java)
    }

    @Value("\${spring.application.name}")
    lateinit var appName: String

    val someTopic get() = topics.someTopic
    val otherTopic get() = topics.otherTopic.toTopic(appName)


    val getInputTopics get() = listOf(someTopic)
    val getOutputTopics get() = listOf(otherTopic)



    // helper functions


    data class Topics(
        // in
        var someTopic: Topic,

        // out
        var otherTopic: ServiceTopic,
    )
    data class Stores(
        val templateDataStore: String
    )

    data class ServiceTopic(
        var name: String,
        var version: String,
        var nameIsFullPath: Boolean = false,
        var retentionMs: Long? = null,
        var retentionBytes: Long? = null,
        var isCompact: Boolean = false,
        var fullTopicPath: String? = null,
        var partitions: Int? = null,
    ) {
        fun toTopic(serviceName: String) = Topic(
            name = name,
            version = version.toInt(),
            serviceName = serviceName,
            nameIsFullPath = nameIsFullPath,
            retentionMs = retentionMs,
            retentionBytes = retentionBytes,
            isCompact = isCompact,
            partitions = partitions
        )
    }

    data class Topic(
        var name: String,
        var version: Int,
        var serviceName: String,
        var nameIsFullPath: Boolean = false,
        var retentionMs: Long? = null,
        var retentionBytes: Long? = null,
        var isCompact: Boolean = false,
        var partitions: Int? = null,
    ){
        val fullTopicPath get() = if (nameIsFullPath) name
            else "${serviceName}-${name}-v${version}"

    }



    @Autowired
    @Suppress("unused")
    fun reportProperties() {
        val version = "0.0.0"
        log.debug(
            "AppStarted -$appName" +
                    "\n\tversion: $version, AppProperties: $this" +
                    "\n\tinputTopics: $getInputTopics" +
                    "\n\toutputTopics: $getOutputTopics"
        )
    }

}


