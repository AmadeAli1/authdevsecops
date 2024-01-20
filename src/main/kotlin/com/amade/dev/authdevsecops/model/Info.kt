package com.amade.dev.authdevsecops.model

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Pattern
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime

data class Info(
    @field:JsonFormat(pattern = "DD MMM YYYY HH:MM:SS") val timestamp: LocalDateTime = LocalDateTime.now(),
    val content: String
)
