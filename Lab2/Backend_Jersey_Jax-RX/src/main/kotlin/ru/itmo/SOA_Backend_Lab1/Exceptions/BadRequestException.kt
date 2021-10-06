package ru.itmo.SOA_Backend_Lab1.Exceptions

import javax.ws.rs.ext.Provider


class BadRequestException(message: String) : Exception(message) {
//    override fun toResponse(exception: BadRequestException): Response {
//        return Response.status(400).entity(exception.message).type("text/plain").build()
//    }

}