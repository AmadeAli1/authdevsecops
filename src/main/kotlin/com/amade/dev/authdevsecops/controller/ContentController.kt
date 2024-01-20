package com.amade.dev.authdevsecops.controller

import com.amade.dev.authdevsecops.model.Info
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RequestMapping("/api/v1/content/")
@RestController
class ContentController {


    @GetMapping("info")
    suspend fun info(@AuthenticationPrincipal principal: Principal): Info {
        val user = principal as UsernamePasswordAuthenticationToken
        return Info(content = "Your email is ${user.principal} and your authorities is ${user.authorities}")
    }


}