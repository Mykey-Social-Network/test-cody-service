package com.mykeyapi.template.utils

import com.mykeyapi.utils.SequenceGenerator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.Jwt
import java.util.*

data class User (val id: String, val isAdmin: Boolean)




@Configuration
@Suppress("unused")
class UUIDGeneratorConfiguration {

    // get value from evn variable name IpAddress
    @Value("\${ipAddress:#{null}}")
    private val ipAddress: String? = null

    private fun getNodeId(): Int? {
        // ipAddress is {ip}.{ip}.{ipX}.{ipY}
        // results should be ipX * ipY else null
        val ip = ipAddress?.split(".")?.map { it.toInt() }
        return ip?.get(2)?.times(ip.get(3))
    }


    @Bean
    fun sequenceGenerator() = SequenceGenerator(nodeId = getNodeId(), 16,6)
}

fun getUser(jwt: Jwt): User =
    User(
        jwt.getClaimAsString("user_id"),
        jwt.claims.getOrDefault("is_admin", false) as Boolean
    )
