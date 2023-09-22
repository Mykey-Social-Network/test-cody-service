package com.mykeyapi.template.api.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@Suppress("unused")


class ApplicationConfig {

//    @Bean
//    fun restTemplate(hmc: ProtobufHttpMessageConverter): RestTemplate? {
//        return RestTemplate(Arrays.asList<HttpMessageConverter<*>>(hmc))
//    }
//
//    @Bean
//    fun protobufHttpMessageConverter(): ProtobufHttpMessageConverter? {
//        return ProtobufHttpMessageConverter()
//    }
//
//    @Bean
//    fun objectMapper(): ObjectMapper {
//        val objectMapper = ObjectMapper()
//        return objectMapper
//    }
//
//    @Bean
//    fun modelResolver(objectMapper: ObjectMapper?): ModelResolver {
//        return ModelResolver(objectMapper)
//    }

    private fun createAPIKeyScheme(): SecurityScheme? {
        return SecurityScheme().type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer")
    }

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI().addSecurityItem(
            SecurityRequirement().addList("Bearer Authentication")
        )
            .components(
                Components().addSecuritySchemes
                    ("Bearer Authentication", createAPIKeyScheme())
            )

    }
}


@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "API Template",
        version = "0.0.1",
        contact = Contact(
            name = "Shlomo",
            email = "shlomo.b@mykey.co.il",
            url = "https://www.mykey.co.il/contact-us/"
        ),
        description = "Template for API"


    ), servers = [Server(url = "http://localhost:9012", description = "Local"),
        Server(url = "https://dev.mykeyapi.com/", description = "Development")]

)
class OpenAPISecurityConfiguration
