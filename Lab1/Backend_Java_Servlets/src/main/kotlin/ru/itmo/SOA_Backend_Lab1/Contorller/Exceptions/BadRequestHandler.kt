package ru.itmo.SOA_Backend_Lab1.Contorller.Exceptions

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "BadRequestHandlerServlet", value = ["/badRequestHandler"])
class BadRequestHandler:HttpServlet() {

    override fun service(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.status = 400
        val error = req.getAttribute("javax.servlet.error.exception") as Throwable
        resp.writer.println(error.message)
    }
}