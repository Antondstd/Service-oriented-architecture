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

@WebServlet(name = "distinctTypeTicketServlet", value = ["/api/additions/distinctTypeTicket"])
class DistinctType:HttpServlet() {

    private var ticketService: TicketService = TicketService()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        req.setCharacterEncoding("utf8")
        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
        val xstream = TicketXstream.getParser()
        val responseAddtitions = ResponseAddtitions(mutableListOf())
        ticketService.getDistinctTypes()?.forEach { (it as TicketType?)?.let { it1 ->
            responseAddtitions.listOfDistictTicketType?.add(
                it1
            )
        } }
        resp.writer.println(xstream.toXML(responseAddtitions))
        cors(resp)
    }

    override fun doOptions(req: HttpServletRequest, resp: HttpServletResponse) {
        cors(resp)
    }

    fun cors(resp: HttpServletResponse) {
        resp.addHeader("Access-Control-Allow-Origin", "*")
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
    }
}