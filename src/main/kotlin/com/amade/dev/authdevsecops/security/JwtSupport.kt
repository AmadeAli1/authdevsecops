package com.amade.dev.authdevsecops.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Component
class JwtSupport(
    environment: Environment,
) {
    private val key = Keys.hmacShaKeyFor(environment["jwt.key"]!!.toByteArray())
    private val parser = Jwts.parserBuilder().setSigningKey(key).build()

    fun generateToken(username: String): BearerToken {
        val jwtBuilder = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date.from(Instant.now()))
            .setExpiration(Date.from(Instant.now().plus(35, ChronoUnit.MINUTES)))
            .signWith(key, SignatureAlgorithm.HS256)
        return BearerToken(jwtBuilder.compact())
    }

    fun getUsername(token: BearerToken): String {
        return parser.parseClaimsJws(token.value).body.subject
    }

    fun isValid(token: BearerToken, userDetails: UserDetails?): Boolean {
        val claims = parser.parseClaimsJws(token.value).body
        val unExpired = claims.expiration.after(Date.from(Instant.now()))
        return unExpired && (claims.subject == userDetails?.username)
    }

    class BearerToken(val value: String) : AbstractAuthenticationToken(AuthorityUtils.NO_AUTHORITIES) {
        override fun getCredentials(): Any = value
        override fun getPrincipal(): Any = value
    }

    data class JwtToken(val accessToken: String)

    data class JwtRefreshToken(val userId: String, val accessToken: String)
}