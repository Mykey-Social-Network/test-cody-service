package com.mykeyapi.template.api

import com.mykeyapi.template.api.model.DataModel
import org.springframework.stereotype.Component


@Component
class RepositoryTemplate {

    fun getData(): DataModel {
        return DataModel("dataId")
    }
}