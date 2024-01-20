package com.amade.dev.authdevsecops.security

import com.amade.dev.authdevsecops.exception.ApiException.InvalidBearerToken
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthManager(
    private val jwtSupport: JwtSupport,
    @Qualifier("AccountService")
    private val userReactiveUserDetailsService: ReactiveUserDetailsService,
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        return Mono.justOrEmpty(authentication)
            .filter { auth -> auth is JwtSupport.BearerToken }
            .cast(JwtSupport.BearerToken::class.java)
            .flatMap { jwtToken -> mono { validate(jwtToken) } }
            .onErrorMap { error ->
                InvalidBearerToken(error.message ?: "Token is not valid")
            }
    }

    private suspend fun validate(token: JwtSupport.BearerToken): Authentication {
        val username = jwtSupport.getUsername(token)
        val user = userReactiveUserDetailsService.findByUsername(username).awaitSingleOrNull()
        if (jwtSupport.isValid(token, user)) {
            return UsernamePasswordAuthenticationToken(user!!.username, user.password, user.authorities)
        }
        throw InvalidBearerToken("Token is not valid")
    }


}