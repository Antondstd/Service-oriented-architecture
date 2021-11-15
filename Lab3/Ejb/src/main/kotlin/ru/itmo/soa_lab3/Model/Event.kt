package ru.itmo.soa_lab3.Model

import ru.itmo.soa_lab3.Exceptions.UnprocessableEntityException
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotNull

@Entity
data class Event(
    @NotNull
    private var name: String? = null,
    @NotNull
    private var date: LocalDateTime? = null,
    @NotNull
    private var minAge: Int? = null
):Serializable {
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