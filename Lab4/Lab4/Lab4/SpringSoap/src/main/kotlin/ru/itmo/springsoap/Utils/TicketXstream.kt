package ru.itmo.SOA_Backend_Lab1.Util


import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.security.AnyTypePermission
import ru.itmo.springsoap.Model.ResponseAddtitions
import ru.itmo.springsoap.Model.ResponsePagesTickets
import ru.itmo.springsoap.Model.Ticket
import ru.itmo.springsoap.Model.TicketType


class TicketXstream {
    companion object{
        private var xstream:XStream? = null
        fun getParser():XStream{
            if (xstream == null){
                xstream = XStream()
                xstream!!.alias("Ticket", Ticket::class.java)
                xstream!!.alias("ResponsePagesTickets", ResponsePagesTickets::class.java)
                xstream!!.alias("list", List::class.java)
                xstream!!.alias("TicketType", TicketType::class.java)
                xstream!!.alias("ResponseAdditions", ResponseAddtitions::class.java)
                xstream!!.registerConverter(LocalDateConvertor())
                xstream!!.registerConverter(ZonedDateConvertor())
                xstream!!.addPermission(AnyTypePermission.ANY)
            }
            return xstream as XStream
        }
    }
}