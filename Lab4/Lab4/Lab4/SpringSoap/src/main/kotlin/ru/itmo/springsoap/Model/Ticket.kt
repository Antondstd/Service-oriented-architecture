package ru.itmo.springsoap.Model


import ru.itmo.springsoap.Exceptions.UnprocessableEntityException
import java.time.ZonedDateTime
import javax.persistence.*
import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType

import kotlin.reflect.full.declaredMemberProperties

@Entity
@XmlRootElement(name = "Ticket")
data class Ticket(
    @XmlAttribute
    var name: String? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    @XmlAttribute
    var coordinates: Coordinates? = null,
//    @NotNull
//    @CreationTimestamp
//    @Column(columnDefinition= "TIMESTAMP WITH TIME ZONE")
//    val creationDate: ZonedDateTime = ZonedDateTime.now(),
    @XmlAttribute
    var price: Long? = null,
    @XmlAttribute
    var discount: Int? = null,
    @XmlAttribute
    var comment: String? = null,
    @XmlAttribute
    var type: TicketType? = null,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    @XmlAttribute
    var event: Event? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @XmlAttribute
    var id: Long = 0

    @Column(name = "creationdate",columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @XmlAttribute
    var creationDate: ZonedDateTime = ZonedDateTime.now()

    init {

    }

    companion object{
        fun getNamesForFilter():List<String> {
            val listOfName: MutableList<String> = mutableListOf()
            Ticket::class.declaredMemberProperties.toMutableList().forEach {
                listOfName.add(it.name)
            }
            listOfName.remove("coordinates")
            listOfName.remove("event")
            Coordinates::class.declaredMemberProperties.toMutableList().forEach {
                listOfName.add("coordinates." + it.name)
            }
            Event::class.declaredMemberProperties.toMutableList().forEach {
                listOfName.add("event." + it.name)
            }
            return listOfName
        }
    }

    fun checkConstrains(){
        val TICKET_PRE = "У объекта Ticket "
        if (name == null)
            throw UnprocessableEntityException(TICKET_PRE + "name не должен быть null")
        if (coordinates == null)
            throw UnprocessableEntityException(TICKET_PRE + "coordinates не должен быть null")
        if (price == null)
            throw UnprocessableEntityException(TICKET_PRE + "price не должен быть null")
        if (discount == null)
            throw UnprocessableEntityException(TICKET_PRE + "discount не должен быть null")
        if (price!! < 1)
            throw UnprocessableEntityException(TICKET_PRE + "price должна быть больше 0")
        if (discount!! < 1 || discount!! > 100)
            throw UnprocessableEntityException(TICKET_PRE + "discount должен быть больше 0 и меньше 101")
        if ((comment?.length ?: 0) > 891)
            throw UnprocessableEntityException(TICKET_PRE + "длина comment не должна превышать 891")
        event?.checkConstrains()
        coordinates?.checkConstrains()
    }

}