package com.mykeyapi.template.api.model.responses

import com.mykeyapi.template.api.model.DataModel

data class GetDataResponseTemplate(
    override val isOk: Boolean = true,
    override val message: String = "",
    val data: DataModel? = null,
) : Response

