package ru.itmo.soa_lab3.Contorller.Additions

import ru.itmo.soa_lab3.Exceptions.BadRequestException
import ru.itmo.soa_lab3.Model.TicketType
import ru.itmo.soa_lab3.Service.TicketServiceInterface
import ru.itmo.soa_lab3.Util.ParamsChecker
import ru.itmo.soa_lab3.Util.RemoteBeanUtil
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.*

//@WebServlet(name = "deleteTypeTicketServlet", value = ["/api/additions/deleteTypeTicket"])
@Path("/additions/deleteTypeTicket")
class DeleteWithType {

    private var ticketServiceRemote: TicketServiceInterface = RemoteBeanUtil.lookupRemoteStatelessBean()

    @DELETE
    fun deleteWithType(@QueryParam("type") typeString:String?):Long {
        if (typeString == null)
            throw BadRequestException("Запрос должен включать параметр type")
        val type:TicketType = ParamsChecker.getAndCheckStringToTicketType(typeString)
        val id = ticketServiceRemote.deleteSomeTicketByType(type)
        return id
    }

    fun cors(resp: HttpServletResponse) {
        resp.addHeader("Access-Control-Allow-Origin", "*")
        resp.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, HEAD, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type")
        resp.addHeader("Access-Control-Allow-Credentials", "true");
    }

}