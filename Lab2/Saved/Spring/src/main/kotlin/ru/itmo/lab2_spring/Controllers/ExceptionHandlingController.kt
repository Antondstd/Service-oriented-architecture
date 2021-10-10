package ru.itmo.lab2_spring.Controllers

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import ru.itmo.lab2_spring.Exceptions.BadRequestException
import ru.itmo.lab2_spring.Exceptions.NotFoundException
@ControllerAdvice
class ExceptionHandlingController {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun notFound(exception: NotFoundException):String{
        return exception.message!!
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException::class)
    fun badRequest(exception: BadRequestException):String{
        return exception.message!!
    }
}