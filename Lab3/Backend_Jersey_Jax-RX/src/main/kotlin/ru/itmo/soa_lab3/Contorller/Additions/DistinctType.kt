package ru.itmo.soa_lab3.Contorller.Additions

import ru.itmo.soa_lab3.Model.ResponseAddtitions
import ru.itmo.soa_lab3.Model.TicketType
import ru.itmo.soa_lab3.Service.TicketServiceInterface
import ru.itmo.soa_lab3.Util.RemoteBeanUtil
import ru.itmo.soa_lab3.Util.TicketXstream
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

//@WebServlet(name = "distinctTypeTicketServlet", value = ["/api/additions/distinctTypeTicket"])
@Path("additions/distinctTypeTicket")
class DistinctType {

    private var ticketServiceRemote: TicketServiceInterface = RemoteBeanUtil.lookupRemoteStatelessBean()

    @GET
    @Produces(MediaType.APPLICATION_XML)
    fun getDistinctType():String {
        val xstream = TicketXstream.getParser()
        val responseAddtitions = ResponseAddtitions(mutableListOf())
        ticketServiceRemote.getDistinctTypes()?.forEach { (it as TicketType?)?.let { it1 ->
            responseAddtitions.listOfDistictTicketType?.add(
                it1
            )
        } }
        return xstream.toXML(responseAddtitions)
    }
}