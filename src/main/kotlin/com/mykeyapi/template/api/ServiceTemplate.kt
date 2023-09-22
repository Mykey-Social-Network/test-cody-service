package com.mykeyapi.template.api

import com.mykeyapi.template.api.model.responses.GetDataResponseTemplate
import org.springframework.stereotype.Component

@Component
class ServiceTemplate(
    private val repositoryTemplate: RepositoryTemplate
) {


    fun getDataModel() : GetDataResponseTemplate {
        val data = repositoryTemplate.getData()
        return GetDataResponseTemplate(
            data = data
        )
    }
}