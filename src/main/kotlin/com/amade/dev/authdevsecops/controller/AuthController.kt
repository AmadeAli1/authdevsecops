package com.amade.dev.authdevsecops.controller

import com.amade.dev.authdevsecops.model.Account
import com.amade.dev.jwtauthapp.model.dto.LoginAccount
import com.amade.dev.authdevsecops.model.dto.RegisterAccount
import com.amade.dev.authdevsecops.security.JwtSupport
import com.amade.dev.authdevsecops.service.AccountService
import jakarta.validation.Valid
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RequestMapping("/api/v1/auth")
@RestController
class AuthController(
    private val accountService: AccountService,
    private val jwtSupport: JwtSupport,
) {

    @PostMapping("/login")
    suspend fun login(@Valid @RequestBody loginAccount: LoginAccount): ResponseEntity<Any> {
        val user = accountService.findByUsername(loginAccount.email).awaitSingleOrNull()
        user?.let {
            if (accountService.isPasswordMatches(loginAccount.password, it.password)) {
                val account = it as Account
                val accessToken = jwtSupport.generateToken(it.username).value
                return ResponseEntity(account.toAccountDTO(accessToken), HttpStatus.OK)
            }
        }

        return ResponseEntity("User not found!", HttpStatus.UNAUTHORIZED)
    }

    @PostMapping("/register")
    suspend fun register(@Valid @RequestBody registerAccount: RegisterAccount): JwtSupport.JwtToken {
        val email = accountService.register(registerAccount)
        return JwtSupport.JwtToken(jwtSupport.generateToken(email).value)
    }

    @PostMapping("/refreshToken")
    suspend fun refreshToken(
        @Valid @RequestBody refreshToken: JwtSupport.JwtRefreshToken,
    ): JwtSupport.JwtToken {
        val account = accountService.findById(refreshToken.userId) as UserDetails
        if (!jwtSupport.isValid(JwtSupport.BearerToken(refreshToken.accessToken), account)) {
            return JwtSupport.JwtToken(
                jwtSupport.generateToken(account.username).value
            )
        }
        throw ResponseStatusException(HttpStatus.NOT_ACCEPTABLE)
    }

}