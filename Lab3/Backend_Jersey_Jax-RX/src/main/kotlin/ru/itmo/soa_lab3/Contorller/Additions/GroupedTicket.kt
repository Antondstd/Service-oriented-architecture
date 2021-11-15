package ru.itmo.soa_lab3.Contorller.Additions

import ru.itmo.soa_lab3.Model.ResponseGroupedDiscount
import ru.itmo.soa_lab3.Service.TicketServiceInterface
import ru.itmo.soa_lab3.Util.RemoteBeanUtil
import ru.itmo.soa_lab3.Util.TicketXstream
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

//@WebServlet(name = "groupedTicketServlet", value = ["/api/additions/groupedDiscountTicket"])
@Path("/additions/groupedDiscountTicket")
class GroupedTicket {

    private var ticketServiceRemote: TicketServiceInterface = RemoteBeanUtil.lookupRemoteStatelessBean()

    @GET
    @Produces(MediaType.APPLICATION_XML)
    fun getGroupedDiscountTicket():String {
//        req.setCharacterEncoding("utf8")
//        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
//        cors(resp)

        val xstream = TicketXstream.getParser()
        xstream.alias("GroupedDiscount", ResponseGroupedDiscount::class.java)
        return xstream.toXML(ticketServiceRemote.getGroupedByDiscount())
    }
}