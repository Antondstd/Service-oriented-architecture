package ru.itmo.soa_lab3.Contorller.Exceptions

import ru.itmo.soa_lab3.Exceptions.BadRequestException
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