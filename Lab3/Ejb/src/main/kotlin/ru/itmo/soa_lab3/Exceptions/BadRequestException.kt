package ru.itmo.soa_lab3.Exceptions


class BadRequestException(message: String) : Exception(message) {
//    override fun toResponse(exception: BadRequestException): Response {
//        return Response.status(400).entity(exception.message).type("text/plain").build()
//    }

}