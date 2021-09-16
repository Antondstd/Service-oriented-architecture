package ru.itmo.SOA_Backend_Lab1.Model

import ru.itmo.SOA_Backend_Lab1.Exceptions.UnprocessableEntityException
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
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private var id:Long = 0

    fun checkConstrains(){
        val COORDINATES_PRE = "У объекта Coordinates "
        if (x == null)
            throw UnprocessableEntityException(COORDINATES_PRE + "y не должен быть меньше -571")
        if (y!! < -571)
            throw UnprocessableEntityException(COORDINATES_PRE + "y не должен быть меньше -571")
    }
}