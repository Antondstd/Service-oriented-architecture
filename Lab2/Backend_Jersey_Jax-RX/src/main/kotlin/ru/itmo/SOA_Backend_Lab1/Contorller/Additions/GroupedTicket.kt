package ru.itmo.SOA_Backend_Lab1.Contorller.Additions

import ru.itmo.SOA_Backend_Lab1.Model.ResponseGroupedDiscount
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

//@WebServlet(name = "groupedTicketServlet", value = ["/api/additions/groupedDiscountTicket"])
@Path("/additions/groupedDiscountTicket")
class GroupedTicket {

    private var ticketService: TicketService = TicketService()

    @GET
    @Produces(MediaType.APPLICATION_XML)
    fun getGroupedDiscountTicket():String {
//        req.setCharacterEncoding("utf8")
//        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
//        cors(resp)

        val xstream = TicketXstream.getParser()
        xstream.alias("GroupedDiscount", ResponseGroupedDiscount::class.java)
        return xstream.toXML(ticketService.getGroupedByDiscount())
    }
}