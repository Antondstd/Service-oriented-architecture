package ru.itmo.soa_lab3.Contorller.Exceptions

import ru.itmo.soa_lab3.Exceptions.EjbNotAvailableException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class EjbNotAvailableHandler: ExceptionMapper<EjbNotAvailableException> {
    override fun toResponse(exception: EjbNotAvailableException): Response {
        return Response.status(503).entity(exception.message).type("text/plain").build()
    }
}