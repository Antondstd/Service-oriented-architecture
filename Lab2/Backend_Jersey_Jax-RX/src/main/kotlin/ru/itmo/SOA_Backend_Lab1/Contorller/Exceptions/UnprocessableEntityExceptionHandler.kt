package ru.itmo.SOA_Backend_Lab1.Contorller.Exceptions

import ru.itmo.SOA_Backend_Lab1.Exceptions.BadRequestException
import ru.itmo.SOA_Backend_Lab1.Exceptions.UnprocessableEntityException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class UnprocessableEntityExceptionHandler: ExceptionMapper<UnprocessableEntityException> {
//    override fun service(req: HttpServletRequest, resp: HttpServletResponse) {
//        resp.status = 422
//        val error = req.getAttribute("javax.servlet.error.exception") as Throwable
//        resp.writer.println(error.message)
//    }
    override fun toResponse(exception: UnprocessableEntityException): Response {
        return Response.status(422).entity(exception.message).type("text/plain").build()
    }
}