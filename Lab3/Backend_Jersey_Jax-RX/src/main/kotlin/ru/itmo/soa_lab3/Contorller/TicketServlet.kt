package ru.itmo.soa_lab3.Contorller

import ru.itmo.soa_lab3.Model.ResponsePagesTickets
import ru.itmo.soa_lab3.Model.Ticket
import ru.itmo.soa_lab3.Service.TicketServiceInterface
import ru.itmo.soa_lab3.Util.RemoteBeanUtil
import ru.itmo.soa_lab3.Util.TicketXstream
import java.nio.charset.Charset
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.UriInfo


//@WebServlet(name = "ticketServlet", value = ["/api/ticket/*"])
@Path("/ticket")
class TicketServlet{

    private var ticketServiceRemote: TicketServiceInterface = RemoteBeanUtil.lookupRemoteStatelessBean()

    @POST
    fun addTicket(body: String): String {
//        req.setCharacterEncoding("utf8")
//        cors(resp)
//        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")

//        val inputStream: InputStream = req.inputStream
//        val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
//        val body = reader.lines().collect(Collectors.joining(System.lineSeparator()))
//        val xml = String(body.encodeToByteArray(), Charset.forName("UTF-8"))
//        println(xml)
        val ticket = ticketServiceRemote.addTicketFromXml(body)
        val xstream = TicketXstream.getParser()
        return xstream.toXML(ticket)
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    fun getTickets(@QueryParam("page") pageNumber:Int, @QueryParam("perPage") perPage:Int, @QueryParam("sortState") sortState: String?, @Context allUri: UriInfo):String {

        val sortStateArray = sortState?.split(',')
        if (sortStateArray != null) {
            println(sortStateArray.size)
        }
        val listOfTicketNames = Ticket.getNamesForFilter()
        val filterMap: Map<String, MutableList<String>> = allUri.queryParameters.toMutableMap().filter { (key, value) ->
            listOfTicketNames.contains(key)
        }
        var responsePagesTickets =
            ticketServiceRemote.getTicketsSortPaging(
                pageNumber,
                perPage,
                sortStateArray,
                filterMap
            )
        if (responsePagesTickets == null)
            responsePagesTickets = ResponsePagesTickets(countAll = 0, perPage = perPage, tickets = listOf<Ticket>())
        val xstream = TicketXstream.getParser()
        return xstream.toXML(responsePagesTickets)
    }

    @GET
    @Path("{id}")
    fun getTicket(@PathParam("id") id: Long):String{
        val xstream = TicketXstream.getParser()
        return xstream.toXML(ticketServiceRemote.getTicket(id))
    }

    @DELETE
    @Path("{id}")
    fun deleteTicket(@PathParam("id") id:Long) {
        ticketServiceRemote.deleteTicket(id)
    }
    @PUT
    @Path("{id}")
    fun updateTicket(body: String, @PathParam("id") id:Long) {
        val xml = String(body.encodeToByteArray(), Charset.forName("UTF-8"))
        ticketServiceRemote.updateTicketFromXml(xml, id)
    }


    fun cors(resp: HttpServletResponse) {
        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
        resp.addHeader("Access-Control-Allow-Origin", "*")
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS")
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type")
        resp.addHeader("Access-Control-Allow-Credentials", "true")
    }
}