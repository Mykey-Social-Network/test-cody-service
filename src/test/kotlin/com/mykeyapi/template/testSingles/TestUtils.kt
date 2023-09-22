package com.mykeyapi.template.testSingles

import org.springframework.security.oauth2.jwt.Jwt
import reactor.core.publisher.Mono

fun getJwt(userId: String = "123456", isAdmin: Boolean = false) = Mono.just(
    Jwt(
        "token", null, null, mapOf("a" to "b"),
        mapOf("user_id" to userId, "isAdmin" to isAdmin))
)


