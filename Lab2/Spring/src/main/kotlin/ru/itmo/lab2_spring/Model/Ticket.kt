package ru.itmo.lab2_spring.Model


import ru.itmo.lab2_spring.Exceptions.UnprocessableEntityException
import java.time.ZonedDateTime
import javax.persistence.*

import kotlin.reflect.full.declaredMemberProperties

@Entity
data class Ticket(

    var name: String? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    var coordinates: Coordinates? = null,
//    @NotNull
//    @CreationTimestamp
//    @Column(columnDefinition= "TIMESTAMP WITH TIME ZONE")
//    val creationDate: ZonedDateTime = ZonedDateTime.now(),

    var price: Long? = null,
    var discount: Int? = null,
    var comment: String? = null,
    var type: TicketType? = null,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    var event: Event? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @Column(name = "creationdate",columnDefinition= "TIMESTAMP WITH TIME ZONE")
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