package ru.itmo.springsoap.Model

import ru.itmo.springsoap.Exceptions.UnprocessableEntityException
import javax.persistence.*
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement

@Entity
@XmlRootElement(name = "Coordinates")
data class Coordinates(
    @XmlAttribute
    var x:Int? = null,
    @XmlAttribute
    var y:Long? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlAttribute
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