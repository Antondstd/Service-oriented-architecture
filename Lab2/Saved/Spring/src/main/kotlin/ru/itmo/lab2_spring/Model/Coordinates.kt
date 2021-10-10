package ru.itmo.lab2_spring.Model

import ru.itmo.lab2_spring.Exceptions.UnprocessableEntityException
import javax.persistence.*

@Entity
data class Coordinates(
    private var x:Int? = null,
    private var y:Long? = null
) {
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