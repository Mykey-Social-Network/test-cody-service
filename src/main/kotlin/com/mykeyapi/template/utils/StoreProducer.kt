package com.mykeyapi.template.utils

import com.mykeyapi.template.configuration.AppProperties
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier
import org.apache.kafka.streams.state.Stores
import org.springframework.stereotype.Component

@Component
class StoreProducer(private val appProperties: AppProperties) {

    fun produceStore(storeName: String): KeyValueBytesStoreSupplier = when (appProperties.useInMemoryStore) {
        true -> Stores.inMemoryKeyValueStore(storeName)
        false -> Stores.persistentKeyValueStore(storeName)
    }
}