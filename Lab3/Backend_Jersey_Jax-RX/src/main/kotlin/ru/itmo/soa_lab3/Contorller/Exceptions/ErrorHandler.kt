package ru.itmo.soa_lab3.Contorller.Exceptions

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "ErrorHandlerServlet", value = ["/errorHandler"])
class ErrorHandler : HttpServlet() {

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
//        resp.status = 400
//        val error = req.getAttribute("javax.servlet.error.exception") as Throwable
        resp.writer.println("SHIIIT")
    }
}