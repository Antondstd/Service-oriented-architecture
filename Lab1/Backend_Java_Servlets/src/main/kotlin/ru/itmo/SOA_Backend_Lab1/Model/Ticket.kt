package ru.itmo.SOA_Backend_Lab1.Model



import org.hibernate.annotations.CreationTimestamp
import ru.itmo.SOA_Backend_Lab1.Exceptions.UnprocessableEntityException
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
) {
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
        val TICKET_PRE = "?? ?????????????? Ticket "
        if (name == null)
            throw UnprocessableEntityException(TICKET_PRE + "name ???? ???????????? ???????? null")
        if (coordinates == null)
            throw UnprocessableEntityException(TICKET_PRE + "coordinates ???? ???????????? ???????? null")
        if (price == null)
            throw UnprocessableEntityException(TICKET_PRE + "price ???? ???????????? ???????? null")
        if (discount == null)
            throw UnprocessableEntityException(TICKET_PRE + "discount ???? ???????????? ???????? null")
        if (price!! < 1)
            throw UnprocessableEntityException(TICKET_PRE + "price ???????????? ???????? ???????????? 0")
        if (discount!! < 1 || discount!! > 100)
            throw UnprocessableEntityException(TICKET_PRE + "discount ???????????? ???????? ???????????? 0 ?? ???????????? 101")
        if ((comment?.length ?: 0) > 891)
            throw UnprocessableEntityException(TICKET_PRE + "?????????? comment ???? ???????????? ?????????????????? 891")
        event?.checkConstrains()
        coordinates?.checkConstrains()
    }

}