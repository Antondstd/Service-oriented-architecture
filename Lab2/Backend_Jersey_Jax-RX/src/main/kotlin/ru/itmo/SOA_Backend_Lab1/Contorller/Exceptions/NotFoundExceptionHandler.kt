package ru.itmo.SOA_Backend_Lab1.Contorller.Exceptions

import ru.itmo.SOA_Backend_Lab1.Exceptions.BadRequestException
import ru.itmo.SOA_Backend_Lab1.Exceptions.NotFoundException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class NotFoundExceptionHandler: ExceptionMapper<NotFoundException> {
    override fun toResponse(exception: NotFoundException): Response {
        return Response.status(404).entity(exception.message).type("text/plain").build()
    }
}