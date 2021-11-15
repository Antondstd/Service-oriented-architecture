package ru.itmo.soa_lab3.Contorller.Exceptions

import ru.itmo.soa_lab3.Exceptions.NotFoundException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class NotFoundExceptionHandler: ExceptionMapper<NotFoundException> {
    override fun toResponse(exception: NotFoundException): Response {
        return Response.status(404).entity(exception.message).type("text/plain").build()
    }
}