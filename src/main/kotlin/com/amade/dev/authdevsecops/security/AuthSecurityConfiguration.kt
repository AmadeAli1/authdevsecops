package com.amade.dev.authdevsecops.security

import com.amade.dev.authdevsecops.model.Role
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class AuthSecurityConfiguration {

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        authenticationManager: JwtAuthManager,
        authConverter: JwtServerAuthenticationConverter,
    ): SecurityWebFilterChain? {
        val filter = AuthenticationWebFilter(authenticationManager)
        filter.setServerAuthenticationConverter(authConverter)
        http
            .authorizeExchange { exchanges: ServerHttpSecurity.AuthorizeExchangeSpec ->
                exchanges
                    .pathMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                    .pathMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                    .pathMatchers(HttpMethod.PUT, "/api/v1/auth/refreshToken").permitAll()
                    .pathMatchers("/api/v1/content/info").hasAuthority(Role.USER.name)
                    .pathMatchers("/api/v1/**").authenticated()
                    .anyExchange().authenticated()
            }
            .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
            .httpBasic { httpBasic -> httpBasic.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .csrf { csrf -> csrf.disable() }
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}