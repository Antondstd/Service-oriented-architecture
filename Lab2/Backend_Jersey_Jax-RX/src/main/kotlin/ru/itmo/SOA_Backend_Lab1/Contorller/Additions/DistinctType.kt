package ru.itmo.SOA_Backend_Lab1.Contorller.Additions

import ru.itmo.SOA_Backend_Lab1.Model.ResponseAddtitions
import ru.itmo.SOA_Backend_Lab1.Model.ResponsePagesTickets
import ru.itmo.SOA_Backend_Lab1.Model.TicketType
import ru.itmo.SOA_Backend_Lab1.Service.TicketService
import ru.itmo.SOA_Backend_Lab1.Util.TicketXstream
import javax.inject.Inject
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

//@WebServlet(name = "distinctTypeTicketServlet", value = ["/api/additions/distinctTypeTicket"])
@Path("additions/distinctTypeTicket")
class DistinctType {

    private var ticketService: TicketService = TicketService()

    @GET
    @Produces(MediaType.APPLICATION_XML)
    fun getDistinctType():String {
        val xstream = TicketXstream.getParser()
        val responseAddtitions = ResponseAddtitions(mutableListOf())
        ticketService.getDistinctTypes()?.forEach { (it as TicketType?)?.let { it1 ->
            responseAddtitions.listOfDistictTicketType?.add(
                it1
            )
        } }
        return xstream.toXML(responseAddtitions)
    }
}