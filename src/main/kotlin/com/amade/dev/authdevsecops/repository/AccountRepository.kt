@file:Suppress("SpringDataRepositoryMethodReturnTypeInspection")

package com.amade.dev.authdevsecops.repository

import com.amade.dev.authdevsecops.model.Account
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AccountRepository : CoroutineCrudRepository<Account, UUID?> {

    suspend fun existsAccountByEmail(email: String):Boolean


    @Query("select * from Account where email=$1")
    suspend fun findByEmail(email: String): Account?


}