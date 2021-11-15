package ru.itmo.soa_lab3.Model



import ru.itmo.soa_lab3.Exceptions.UnprocessableEntityException
import java.io.Serializable
import java.time.ZonedDateTime
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


import javax.validation.constraints.Size
import kotlin.reflect.full.declaredMemberProperties

@Entity
data class Ticket(
    @NotNull
    var name: String? = null,
    @NotNull
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    var coordinates: Coordinates? = null,
//    @NotNull
//    @CreationTimestamp
//    @Column(columnDefinition= "TIMESTAMP WITH TIME ZONE")
//    val creationDate: ZonedDateTime = ZonedDateTime.now(),
    @NotNull
    @Min(0)
    var price: Long? = null,
    @NotNull
    @Min(0)
    @Max(100)
    var discount: Int? = null,
    @Size(max = 891)
    var comment: String? = null,
    var type: TicketType? = null,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn
    var event: Event? = null
):Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long = 0

    @NotNull
    @Column(columnDefinition= "TIMESTAMP WITH TIME ZONE")
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