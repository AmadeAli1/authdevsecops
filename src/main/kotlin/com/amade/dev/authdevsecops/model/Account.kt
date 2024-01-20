package com.amade.dev.authdevsecops.model

import com.amade.dev.authdevsecops.model.dto.AccountDTO
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Table("Account")
data class Account(
    @Id @Column("id") val id: UUID? = null,
    @Column("email") val email: String,
    @Column("password") private val password: String,
    @Column("username") private val username: String,
    @Column("role") private val role: Role,
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun toAccountDTO(token: String): AccountDTO {
        return AccountDTO(id = id.toString(), email = email, username = username, role = role, accessToken = token)
    }

}
