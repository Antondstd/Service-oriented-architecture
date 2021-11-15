package ru.itmo.lab2_spring.Model


import ru.itmo.lab2_spring.Exceptions.UnprocessableEntityException
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Event(

    private var name: String? = null,

    private var date: LocalDateTime? = null,
    @Column(name = "minage")
    private var minAge: Int? = null
//    private var ticketsCount:Long = 0,
//    private var eventType:EventType = EventType.BASEBALL
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    fun checkConstrains() {
        val EVENT_PRE = "У объекта Event "
        if (name == null)
            throw UnprocessableEntityException(EVENT_PRE + "name не должен быть null")
        if (date == null)
            throw UnprocessableEntityException(EVENT_PRE + "date не должен быть null")
        if (minAge == null)
            throw UnprocessableEntityException(EVENT_PRE + "minAge не должен быть null")
    }
}