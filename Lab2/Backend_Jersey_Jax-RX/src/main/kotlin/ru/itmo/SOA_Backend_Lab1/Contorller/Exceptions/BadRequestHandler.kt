package ru.itmo.SOA_Backend_Lab1.Contorller.Exceptions

import ru.itmo.SOA_Backend_Lab1.Exceptions.BadRequestException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.Path
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

//@Path("/badRequestHandler")
@Provider
class BadRequestHandler: ExceptionMapper<BadRequestException> {
    override fun toResponse(exception: BadRequestException): Response {
        return Response.status(400).entity(exception.message).type("text/plain").build()
    }
}