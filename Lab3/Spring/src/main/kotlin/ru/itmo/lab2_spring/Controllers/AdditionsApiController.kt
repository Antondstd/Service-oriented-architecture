package ru.itmo.lab2_spring.Controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import ru.itmo.lab2_spring.Services.TicketService
import ru.itmo.lab2_spring.Utils.ConfigurationUtil
import ru.itmo.lab2_spring.Utils.RestTemplateResponseErrorHandler

@RestController
@RequestMapping("/additions")
class AdditionsApiController {
    val restTemplate = RestTemplateBuilder().errorHandler(RestTemplateResponseErrorHandler()).build()

    @Autowired
    lateinit var configurationUtil: ConfigurationUtil
    val url = "/additions"

    @Autowired
    lateinit var ticketService:TicketService


    @GetMapping("/distinctTypeTicket")
    fun getDistinctTypes():String{
        return ticketService.getDistTypesFromAPI()
    }

    @GetMapping("/groupedDiscountTicket")
    fun getGroupedDiscountTicket(@RequestParam allRequestParams:Map<String,String>):String{
        return ticketService.getGroupedDicountFromAPI()
    }


    @DeleteMapping("/deleteTypeTicket")
    fun deleteTypeTicket(@RequestParam("type") type:String){
        return ticketService.deleteTypeTicket(type)
    }
}