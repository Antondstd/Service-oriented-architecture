package ru.itmo.soa_lab3.Contorller.Exceptions

import ru.itmo.soa_lab3.Exceptions.UnprocessableEntityException
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