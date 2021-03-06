package ru.itmo.springsoap.Utils

import org.springframework.stereotype.Component
import org.springframework.ws.context.MessageContext
import org.springframework.ws.server.endpoint.AbstractEndpointExceptionResolver
import org.springframework.ws.soap.SoapFault
import org.springframework.ws.soap.SoapFaultDetail
import org.springframework.ws.soap.SoapMessage
import ru.itmo.springsoap.Exceptions.BadRequestException
import ru.itmo.springsoap.Exceptions.NotFoundException
import ru.itmo.springsoap.generated.ObjectFactory
import java.util.*
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.namespace.QName


@Component
class CustomExceptionResolver: AbstractEndpointExceptionResolver() {

    override fun resolveExceptionInternal(
        messageContext: MessageContext,
        endpoint: Any?,
        exception: Exception?
    ): Boolean {
        if (exception is NotFoundException || exception is BadRequestException) {
            val response = messageContext.getResponse() as SoapMessage
            val soapBody = response.soapBody
            val soapFault: SoapFault = soapBody.addClientOrSenderFault(
                exception.javaClass.name,
                Locale.ENGLISH
            )
            val detail = soapFault.addFaultDetail()
            detail.addFaultDetailElement(QName("code")).addText(if (exception is NotFoundException) "404" else "400")
            detail.addFaultDetailElement(QName("message")).addText(exception.message)
            return true
        }
        return false
    }
}