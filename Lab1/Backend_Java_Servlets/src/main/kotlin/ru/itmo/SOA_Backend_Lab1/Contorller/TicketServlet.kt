package ru.itmo.SOA_Backend_Lab1.Contorller

import ru.itmo.SOA_Backend_Lab1.Exceptions.BadRequestException
import ru.itmo.SOA_Backend_Lab1.Model.ResponsePagesTickets
import ru.itmo.SOA_Backend_Lab1.Model.Ticket
import ru.itmo.SOA_Backend_Lab1.Service.TicketService
import ru.itmo.SOA_Backend_Lab1.Util.ParamsChecker
import ru.itmo.SOA_Backend_Lab1.Util.TicketXstream
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet(name = "ticketServlet", value = ["/api/ticket/*"])
class TicketServlet : HttpServlet() {

    private var ticketService: TicketService = TicketService()

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        req.setCharacterEncoding("utf8")
        cors(resp)
        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")

        val inputStream: InputStream = req.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
        val body = reader.lines().collect(Collectors.joining(System.lineSeparator()))
        val ticket = ticketService.addTicketFromXml(body)
        val xstream = TicketXstream.getParser()
        resp.writer.println(xstream.toXML(ticket))
    }

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        cors(resp)
        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
        val pageNumber = ParamsChecker.getAndCheckStringToPositiveLong("page",req.getParameter("page")).toInt()
        val perPage = ParamsChecker.getAndCheckStringToPositiveLong("perPage",req.getParameter("perPage")).toInt()

        val sortStateArray = req.getParameter("sortState")?.split(',')
        println(sortStateArray)
        if (sortStateArray != null) {
            println(sortStateArray.size)
        }
        val listOfTicketNames = Ticket.getNamesForFilter()
        val filterMap: Map<String, Array<String>> = req.parameterMap.toMutableMap().filter { (key, value) ->
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
        resp.writer.println(xstream.toXML(responsePagesTickets))
    }

    override fun doDelete(req: HttpServletRequest, resp: HttpServletResponse) {
        req.setCharacterEncoding("utf8");
        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
        val id: Long = ParamsChecker.getAndcheckIfPathHasID(req.pathInfo)
        cors(resp)
        ticketService.deleteTicket(id)
    }

    override fun doPut(req: HttpServletRequest, resp: HttpServletResponse) {
        req.setCharacterEncoding("utf8");
        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
        val inputStream: InputStream = req.inputStream
        val reader = BufferedReader(InputStreamReader(inputStream, StandardCharsets.UTF_8))
        val body = reader.lines().collect(Collectors.joining(System.lineSeparator()))
        val id: Long = ParamsChecker.getAndcheckIfPathHasID(req.pathInfo)
        ticketService.updateTicketFromXml(body,id)
    }

    override fun doOptions(req: HttpServletRequest, resp: HttpServletResponse) {
        cors(resp)
    }


    fun cors(resp: HttpServletResponse) {
        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE");
        resp.addHeader("Access-Control-Allow-Origin", "*")
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
    }
}