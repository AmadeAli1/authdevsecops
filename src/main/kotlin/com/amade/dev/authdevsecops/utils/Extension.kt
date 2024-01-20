package com.amade.dev.authdevsecops.utils

import java.util.*

fun String.toUUID(): UUID {
    return UUID.fromString(this)
}