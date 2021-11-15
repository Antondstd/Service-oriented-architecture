package ru.itmo.lab2_spring.Services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder
import ru.itmo.SOA_Backend_Lab1.Util.TicketXstream
import ru.itmo.lab2_spring.Exceptions.BadRequestException
import ru.itmo.lab2_spring.Exceptions.NotFoundException
import ru.itmo.lab2_spring.Model.ResponsePagesTickets
import ru.itmo.lab2_spring.Model.Ticket
import ru.itmo.lab2_spring.Model.TicketType
import ru.itmo.lab2_spring.Repository.TicketRepository
import ru.itmo.lab2_spring.Utils.ConfigurationUtil
import ru.itmo.lab2_spring.Utils.RestTemplateConfig
import java.net.URI
import java.util.*


@Service
class TicketService(private val ticketRepository: TicketRepository):TicketRepository by ticketRepository {

    @Autowired
    lateinit var configurationUtil: ConfigurationUtil

    @Autowired
    lateinit var restTemplate:RestTemplateConfig

    val urlToTicket = "/ticket"
    val urlToAdditions = "/additions"

    fun makeVipAndDoubleCost(id:Long):String{
        val ticket = getTicketFromAPI(id)
        ticket?.apply {
            price = price!! * 2
            type = TicketType.VIP
        }
        val xstream = TicketXstream.getParser()
        return addTicketAsXMLByAPI(xstream.toXML(ticket))
    }

    fun getTicketsAsStringFromAPI(allRequestParams:Map<String,String>):String{
        val builder = UriComponentsBuilder.fromUriString(configurationUtil.urlApiService() + urlToTicket). apply {
            for (param in allRequestParams)
                queryParam(param.key,param.value)
        }.build().toUriString()
        return restTemplate.restTemplate(RestTemplateBuilder()).getForObject(builder,String::class.java)!!
    }

    fun getTicketAsStringFromAPI(id:Long):String{
        return restTemplate.restTemplate(RestTemplateBuilder()).getForObject(configurationUtil.urlApiService() + urlToTicket + "/{id}",String::class.java,id)!!
    }

    fun getTicketFromAPI(id:Long):Ticket?{
        val xstream = TicketXstream.getParser()
        val newTicket: Ticket
        val xmlTicket = getTicketAsStringFromAPI(id)
        try {
            newTicket = xstream.fromXML(xmlTicket) as Ticket
        } catch (exception: Exception) {
            throw BadRequestException("Невалидный xml")
        }
        return newTicket
    }

    fun updateTicketXMLByAPI(id: Long,xml:String){
        restTemplate.restTemplate(RestTemplateBuilder()).put(configurationUtil.urlApiService() + urlToTicket + "/{id}",xml,id)
    }

    fun addTicketAsXMLByAPI(ticketXML:String):String{
        return restTemplate.restTemplate(RestTemplateBuilder()).postForEntity(configurationUtil.urlApiService() + urlToTicket,ticketXML,String::class.java).body!!
    }

    fun deleteTicketAPI(id: Long){
        restTemplate.restTemplate(RestTemplateBuilder()).delete(configurationUtil.urlApiService() + urlToTicket + "/{id}",id)
    }

    fun cancelEvent(eventId:Long){
        val param = mutableMapOf<String,String>().apply {
            put("event.id",eventId.toString())
            put("page","1")
            put("perPage","1")
        }
        val xstream = TicketXstream.getParser()
        var listOfTicket:List<Ticket>?
        val xml = getTicketsAsStringFromAPI(param)
//        try {
            listOfTicket = (xstream.fromXML(xml) as ResponsePagesTickets).tickets
            if (listOfTicket != null) {
                if (listOfTicket.size == 0)
                    throw NotFoundException("Не был найдет билет с event.id = $eventId")
                for (ticket in listOfTicket){
                    deleteTicketAPI(ticket.id)
                }
            }
    }

    fun getDistTypesFromAPI():String{
        return restTemplate.restTemplate(RestTemplateBuilder()).getForObject(configurationUtil.urlApiService() + urlToAdditions + "/distinctTypeTicket",String::class.java)!!
    }

    fun getGroupedDicountFromAPI():String{
        return restTemplate.restTemplate(RestTemplateBuilder()).getForObject(configurationUtil.urlApiService() + urlToAdditions + "/groupedDiscountTicket",String::class.java)!!
    }

    fun deleteTypeTicket(type:String){
        val builder = UriComponentsBuilder.fromUriString(configurationUtil.urlApiService() + urlToAdditions + "/deleteTypeTicket"). apply {
            queryParam("type",type)
        }.build().toUriString()
        restTemplate.restTemplate(RestTemplateBuilder()).delete(builder,type)
    }





}