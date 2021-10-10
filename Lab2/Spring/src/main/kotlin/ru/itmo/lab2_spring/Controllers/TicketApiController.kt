package ru.itmo.lab2_spring.Controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.*
import ru.itmo.lab2_spring.Services.TicketService
import ru.itmo.lab2_spring.Utils.ConfigurationUtil
import ru.itmo.lab2_spring.Utils.RestTemplateResponseErrorHandler
import java.nio.charset.Charset

@RestController
@RequestMapping("/tickets")
class TicketApiController {

    @Autowired
    lateinit var configurationUtil: ConfigurationUtil

    @Autowired
    lateinit var ticketService: TicketService

    val restTemplate = RestTemplateBuilder().errorHandler(RestTemplateResponseErrorHandler()).build()
    val url = "/ticket"

    @GetMapping
    fun getTickets(@RequestParam allRequestParams: Map<String, String>): String {
        return ticketService.getTicketsAsStringFromAPI(allRequestParams)
    }

    @GetMapping("/{id}")
    fun getTicket(@PathVariable(value = "id") ticketID: Long) {

    }

    @PostMapping
    fun addTicket(@RequestBody xml:String): String {
//        println("ADDING TICKET")
//        val xml = String(xml.encodeToByteArray(), Charset.forName("UTF-8"))
        return ticketService.addTicketAsXMLByAPI(xml)
    }

    @PutMapping("/{id}")
    fun updateTicket(@RequestBody xml:String, @PathVariable(value = "id") ticketID: Long) {
        ticketService.updateTicketXMLByAPI(ticketID, xml)
    }

    @DeleteMapping("/{id}")
    fun deleteTicket(@PathVariable(value = "id") ticketId: Long) {
        ticketService.deleteTicketAPI(ticketId)
    }
}