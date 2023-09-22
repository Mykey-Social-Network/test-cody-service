package com.mykeyapi.template.api

import com.mykeyapi.template.api.model.responses.GetDataResponseTemplate
import com.mykeyapi.template.utils.getUser
import io.swagger.v3.oas.annotations.Operation
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@Suppress("unused")
class ControllerTemplate(
    private val serviceTemplate: ServiceTemplate,
) {
    private val log = LoggerFactory.getLogger(ControllerTemplate::class.java)

    @Operation(summary = "Get data endpoint template")
    @GetMapping("template/someEndPoint")
    fun getDataTemplate(@AuthenticationPrincipal jwt: Mono<Jwt>): Mono<GetDataResponseTemplate> {
        return jwt.map(::getUser).map { user ->
            log.info("user: $user")
            serviceTemplate.getDataModel()
        }
    }
}

