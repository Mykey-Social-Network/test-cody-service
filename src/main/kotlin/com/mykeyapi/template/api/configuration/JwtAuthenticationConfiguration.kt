package com.mykeyapi.template.api.configuration


import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtReactiveAuthenticationManager
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter
import org.springframework.security.web.server.SecurityWebFilterChain
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Configuration
@EnableWebFluxSecurity
@Suppress("unused")
class JwtAuthenticationConfiguration {

    @Value("\${jwt.public_key}")
    private lateinit var publicKey: String

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .logout().disable()
            .authenticationManager(authenticationManager())
            .authorizeExchange()
            .pathMatchers("/template/api-docs/**").permitAll() // todo: edit
            .pathMatchers("/template/swagger-ui/**").permitAll()
            .pathMatchers("/template/swagger-ui.html").permitAll()
            .pathMatchers("/template/webjars/**").permitAll()
            .pathMatchers("/actuator/**").permitAll()
            .pathMatchers("/**").authenticated()
            .and()

        http.oauth2ResourceServer()
            .jwt()

        return http.build()
    }


    @Bean
    fun authenticationManager(): ReactiveAuthenticationManager {
        val authenticationManager = JwtReactiveAuthenticationManager(reactiveJwtDecoder())
        val authenticationConverter = ReactiveJwtAuthenticationConverterAdapter(JwtAuthenticationConverter())
        authenticationManager.setJwtAuthenticationConverter(authenticationConverter)
        return authenticationManager
    }

    @Bean
    fun reactiveJwtDecoder(): ReactiveJwtDecoder {
        val keySpecX509 = X509EncodedKeySpec(Base64.getDecoder().decode(publicKey))
        val pubKey = KeyFactory.getInstance("RSA").generatePublic(keySpecX509) as RSAPublicKey
        return NimbusReactiveJwtDecoder.withPublicKey(pubKey)
            .build()
    }


}
