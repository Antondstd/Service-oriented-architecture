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

@WebServlet(name = "groupedTicketServlet", value = ["/api/additions/groupedDiscountTicket"])
class GroupedTicket:HttpServlet() {

    private var ticketService: TicketService = TicketService()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        req.setCharacterEncoding("utf8")
        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE")
        cors(resp)

        val xstream = TicketXstream.getParser()
        xstream.alias("GroupedDiscount", ResponseGroupedDiscount::class.java)
        resp.writer.println(xstream.toXML(ticketService.getGroupedByDiscount()))
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