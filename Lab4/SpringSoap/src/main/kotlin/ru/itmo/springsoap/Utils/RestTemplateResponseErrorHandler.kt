package ru.itmo.springsoap.Utils


import org.springframework.http.HttpStatus
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import ru.itmo.springsoap.Exceptions.BadRequestException
import ru.itmo.springsoap.Exceptions.NotFoundException
import java.io.IOException


@Component
class RestTemplateResponseErrorHandler : ResponseErrorHandler {
    @Throws(IOException::class)
    override fun hasError(httpResponse: ClientHttpResponse): Boolean {
        return (httpResponse.getStatusCode().series() === HttpStatus.Series.CLIENT_ERROR
                || httpResponse.getStatusCode().series() === HttpStatus.Series.SERVER_ERROR)
    }

    @Throws(IOException::class)
    override fun handleError(httpResponse: ClientHttpResponse) {
        if (httpResponse.getStatusCode()
                .series() === HttpStatus.Series.SERVER_ERROR
        ) {
            // handle SERVER_ERROR
        } else if (httpResponse.getStatusCode()
                .series() === HttpStatus.Series.CLIENT_ERROR
        ) {
            // handle CLIENT_ERROR
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw NotFoundException(httpResponse.body.reader(Charsets.UTF_8).use { it.readText() })
            }
            if (httpResponse.rawStatusCode == 400 || httpResponse.rawStatusCode == 422)
            {
                println("THROWING BADREQUEST")
                throw BadRequestException(httpResponse.body.reader(Charsets.UTF_8).use { it.readText() })
            }
        }
    }
}