package ru.itmo.soa_lab3.Contorller.Exceptions

import ru.itmo.soa_lab3.Exceptions.BadRequestException
import ru.itmo.soa_lab3.Exceptions.UnprocessableEntityException
import javax.ejb.EJBException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class EJBExceptionHandler : ExceptionMapper<EJBException> {
    override fun toResponse(exception: EJBException): Response {
        val nestedException = exception.cause!!.cause!!.cause!!
        if (nestedException is BadRequestException || nestedException is UnprocessableEntityException)
            return Response.status(400).entity(nestedException.message).type("text/plain").build()
        else
            return Response.status(404).header("Content-Type", "text/xml; charset=UTF-16LE")
                .entity(nestedException.message).type("text/plain").build()
    }
}