package ru.itmo.lab2_spring.Controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/booking")
class Booking {
    @GetMapping("/sell/vip/ticket-id/person-id")
    fun test(): String {
        return "Hello"
    }
}