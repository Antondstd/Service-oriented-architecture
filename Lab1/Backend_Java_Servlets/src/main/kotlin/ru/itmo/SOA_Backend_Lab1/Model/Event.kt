package ru.itmo.SOA_Backend_Lab1.Model

import ru.itmo.SOA_Backend_Lab1.Exceptions.UnprocessableEntityException
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
data class Event(
    @NotNull
    private var name:String? = null,
    @NotNull
    private var date:LocalDateTime? = null,
    @NotNull
    private var minAge:Int? = null
//    private var ticketsCount:Long = 0,
//    private var eventType:EventType = EventType.BASEBALL
){
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private var id:Long =0

    fun checkConstrains(){
        val EVENT_PRE = "У объекта Event "
        if (name == null)
            throw UnprocessableEntityException(EVENT_PRE + "name не должен быть null")
        if (date == null)
            throw UnprocessableEntityException(EVENT_PRE + "date не должен быть null")
        if (minAge == null)
            throw UnprocessableEntityException(EVENT_PRE + "minAge не должен быть null")
    }
}