package ru.itmo.soa_lab3.Model

import java.io.Serializable

enum class TicketType():Serializable {
    VIP,
    USUAL,
    BUDGETARY,
    CHEAP
}