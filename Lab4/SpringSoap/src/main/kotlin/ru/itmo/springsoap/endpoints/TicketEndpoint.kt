package ru.itmo.springsoap.endpoints

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import ru.itmo.springsoap.Services.TicketService
import ru.itmo.springsoap.Utils.JaxBLocalDateTimeAdapter
import ru.itmo.springsoap.Utils.JaxBZonedDateAdapter
import ru.itmo.springsoap.generated.*

@Endpoint
class TicketEndpoint {

    @Autowired
    lateinit var ticketService: TicketService

    @PayloadRoot(namespace = "http://www.itmo.ru/springsoap/generated", localPart = "makeVipAndDoubleCostRequest")
    @ResponsePayload
    fun saleVip(@RequestPayload request: MakeVipAndDoubleCostRequest): MakeVipAndDoubleCostResponse {
        val ticket = ticketService.makeVipAndDoubleCost(request.id)
        val response = MakeVipAndDoubleCostResponse()
        response.ticket = TicketDTO().apply {
            id = ticket.id
            creationDate = JaxBZonedDateAdapter().marshal(ticket.creationDate)
            coordinates = Coordinates()
            coordinates.id = ticket.coordinates!!.id
            coordinates.x = ticket.coordinates!!.x!!
            coordinates.y = ticket.coordinates!!.y!!
            price = ticket.price
            discount = ticket.discount
            comment = ticket.comment
            type =  TicketType.values()[ticket.type!!.ordinal]
            event = EventDTO()
            event.id = ticket.event!!.id
            event.name = ticket.event!!.name
            event.date = ticket.event!!.date?.let { JaxBLocalDateTimeAdapter().marshal(it) }
            event.minAge = ticket.event!!.minAge!!
        }
        return response
    }

    @PayloadRoot(namespace = "http://www.itmo.ru/springsoap/generated", localPart = "cancelEventRequest")
    @ResponsePayload
    fun cancelEvent(@RequestPayload request: CancelEventRequest):CancelEventResponse{
        ticketService.cancelEvent(request.eventId)
        return CancelEventResponse().apply { id =  request.eventId}
    }
}