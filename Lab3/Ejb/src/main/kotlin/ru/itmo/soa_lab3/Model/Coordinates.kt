package ru.itmo.soa_lab3.Model

import ru.itmo.soa_lab3.Exceptions.UnprocessableEntityException
import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
data class Coordinates(
    @NotNull
    private var x:Int? = null,
    @Min(-571)
    private var y:Long? = null
):Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id:Long = 0

    fun checkConstrains(){
        val COORDINATES_PRE = "У объекта Coordinates "
        if (x == null)
            throw UnprocessableEntityException(COORDINATES_PRE + "x не должен быть null")
        if (y == null)
            throw UnprocessableEntityException(COORDINATES_PRE + "y не должен быть null")
        if (y!! < -571)
            throw UnprocessableEntityException(COORDINATES_PRE + "y не должен быть меньше -571")
    }
}