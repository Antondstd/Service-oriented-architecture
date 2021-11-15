package ru.itmo.soa_lab3.Contorller.Exceptions

import java.rmi.RemoteException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class RemoteExceptionHandler: ExceptionMapper<RemoteException> {
    override fun toResponse(exception: RemoteException): Response {
//        if (exception.cause.cause is BadRequestException)
//            if (e.getCause() != null && e.getCause().getCause() is MyException) {
//                val ex: MyException = e.getCause().getCause() as MyException
//                // Do further useful stuff
//            }
        throw exception.cause!!.cause!!
//        return Response.status(exception.).entity(exception.message).type("text/plain").build()
    }
}