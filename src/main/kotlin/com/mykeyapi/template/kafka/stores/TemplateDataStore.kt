package com.mykeyapi.template.kafka.stores


import com.mykeyapi.models.EventTemplateOuterClass.EventTemplate
import com.mykeyapi.template.configuration.AppProperties
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.streams.StoreQueryParameters
import org.apache.kafka.streams.state.QueryableStoreTypes
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore
import org.springframework.kafka.config.StreamsBuilderFactoryBean
import org.springframework.stereotype.Component


@Component
@Suppress("unused")
class TemplateDataStore(
    private val appProperties: AppProperties,
    private val streamsBuilderFactoryBean: StreamsBuilderFactoryBean,
    private val stringSerde: Serde<String>,
    private val eventTemplateSerde: Serde<EventTemplate>,
) {

    val templateDataStore: ReadOnlyKeyValueStore<String, EventTemplate>
        get() = streamsBuilderFactoryBean.kafkaStreams!!.store(
            StoreQueryParameters.fromNameAndType(
                appProperties.stores.templateDataStore,
                QueryableStoreTypes.keyValueStore()
            )
        )


    fun getTemplateData(dataId: String): EventTemplate? {
        return templateDataStore.get(dataId)
    }
}