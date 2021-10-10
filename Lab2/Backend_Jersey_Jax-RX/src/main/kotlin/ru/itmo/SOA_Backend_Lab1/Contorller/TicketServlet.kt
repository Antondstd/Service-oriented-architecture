package ru.itmo.SOA_Backend_Lab1.Contorller

import ru.itmo.SOA_Backend_Lab1.Model.ResponsePagesTickets
import ru.itmo.SOA_Backend_Lab1.Model.Ticket
import ru.itmo.SOA_Backend_Lab1.Service.TicketService
import ru.itmo.SOA_Backend_Lab1.Util.ParamsChecker
import ru.itmo.SOA_Backend_Lab1.Util.TicketXstream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.*
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.UriInfo


//@WebServlet(name = "ticketServlet", value = ["/api/ticket/*"])
@Path("/ticket")
class TicketServlet{

    private var ticketService: TicketService = TicketService()

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
        val ticket = ticketService.addTicketFromXml(body)
        val xstream = TicketXstream.getParser()
        return xstream.toXML(ticket)
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    fun getTickets(@QueryParam("page") pageNumber:Int, @QueryParam("perPage") perPage:Int, @QueryParam("sortState") sortState: String?, @Context allUri: UriInfo):String {
//        cors(resp)
//        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
//        val pageNumber = ParamsChecker.getAndCheckStringToPositiveLong("page", req.getParameter("page")).toInt()
//        val perPage = ParamsChecker.getAndCheckStringToPositiveLong("perPage", req.getParameter("perPage")).toInt()

        val sortStateArray = sortState?.split(',')
//        println(sortStateArray)
        if (sortStateArray != null) {
            println(sortStateArray.size)
        }
        val listOfTicketNames = Ticket.getNamesForFilter()
        val filterMap: Map<String, MutableList<String>> = allUri.queryParameters.toMutableMap().filter { (key, value) ->
            listOfTicketNames.contains(key)
        }
        var responsePagesTickets =
            ticketService.`get Ticket with sorting,paging and filtering`(
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
        return xstream.toXML(ticketService.getTicket(id))
    }

    @DELETE
    @Path("{id}")
    fun deleteTicket(@PathParam("id") id:Long) {
//        req.setCharacterEncoding("utf8");
//        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
//        val id: Long = ParamsChecker.getAndcheckIfPathHasID(req.pathInfo)
        ticketService.deleteTicket(id)
    }
    @PUT
    @Path("{id}")
    fun updateTicket(body: String, @PathParam("id") id:Long) {
//        req.setCharacterEncoding("utf8");
//        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
//        val inputStream: InputStream = req.inputStream
//        val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
//        val body = reader.lines().collect(Collectors.joining(System.lineSeparator()))
//        val id: Long = ParamsChecker.getAndcheckIfPathHasID(req.pathInfo)
        val xml = String(body.encodeToByteArray(), Charset.forName("UTF-8"))
        ticketService.updateTicketFromXml(xml, id)
    }
//    @OPTIONS
//    fun doOptions(req: HttpServletRequest, resp: HttpServletResponse) {
//        cors(resp)
//    }


    fun cors(resp: HttpServletResponse) {
        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
        resp.addHeader("Access-Control-Allow-Origin", "*")
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS")
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type")
        resp.addHeader("Access-Control-Allow-Credentials", "true")
    }
}