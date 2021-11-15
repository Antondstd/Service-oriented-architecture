package ru.itmo.lab2_spring.Controllers

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.web.bind.annotation.*

import ru.itmo.lab2_spring.Services.TicketService


@RestController
@RequestMapping("/booking")
class Booking {

    @Autowired
    lateinit var ticketService: TicketService

    @PostMapping("/sell/vip/{ticket-id}")
    @ResponseBody
    fun sellVipTicket(@PathVariable(value = "ticket-id") ticketId: Long): String {
        return ticketService.makeVipAndDoubleCost(ticketId)
    }

    @DeleteMapping("/event/{event-id}/cancel")
    fun cancelEvent(@PathVariable(value = "event-id") eventId: Long) {
        ticketService.cancelEvent(eventId)
    }
}