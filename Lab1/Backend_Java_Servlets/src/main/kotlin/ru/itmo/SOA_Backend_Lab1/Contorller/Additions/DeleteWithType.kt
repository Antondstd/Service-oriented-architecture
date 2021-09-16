package ru.itmo.SOA_Backend_Lab1.Contorller.Additions

import ru.itmo.SOA_Backend_Lab1.Exceptions.BadRequestException
import ru.itmo.SOA_Backend_Lab1.Model.TicketType
import ru.itmo.SOA_Backend_Lab1.Service.TicketService
import ru.itmo.SOA_Backend_Lab1.Util.ParamsChecker
import javax.inject.Inject
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "deleteTypeTicketServlet", value = ["/api/additions/deleteTypeTicket"])
class DeleteWithType: HttpServlet() {

    private var ticketService: TicketService = TicketService()

    override fun doDelete(req: HttpServletRequest, resp: HttpServletResponse) {
        req.setCharacterEncoding("utf8")
        cors(resp)
        resp.setHeader("Content-Type", "text/xml; charset=UTF-16LE");
        val typeString = req.getParameter("type")
        if (typeString == null)
            throw BadRequestException("Запрос должен включать параметр type")
        val type:TicketType = ParamsChecker.getAndCheckStringToTicketType(typeString)
        val id = ticketService.deleteSomeTicketByType(type)
        resp.writer.println("$id")
    }

    override fun doOptions(req: HttpServletRequest, resp: HttpServletResponse) {
        cors(resp)
    }

    fun cors(resp: HttpServletResponse) {
        resp.addHeader("Access-Control-Allow-Origin", "*")
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type")
        resp.addHeader("Access-Control-Allow-Credentials", "true");
    }

}