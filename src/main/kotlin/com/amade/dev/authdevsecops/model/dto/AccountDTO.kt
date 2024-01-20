package com.amade.dev.authdevsecops.model.dto

import com.amade.dev.authdevsecops.model.Role

data class AccountDTO(
    val id: String,
    val email: String,
    val username: String,
    val role: Role,
    val accessToken: String,
)
