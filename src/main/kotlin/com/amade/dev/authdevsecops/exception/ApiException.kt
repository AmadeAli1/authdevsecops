package com.amade.dev.authdevsecops.exception

sealed class ApiException(message: String) : RuntimeException(message) {

    class RegisterAccountException(message: String) : ApiException(message)

    class AccountNotFoundException(message: String) : ApiException(message)

    class InvalidBearerToken(message: String) : ApiException(message)

}