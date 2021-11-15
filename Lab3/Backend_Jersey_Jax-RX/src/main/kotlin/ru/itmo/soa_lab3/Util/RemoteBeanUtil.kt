package ru.itmo.soa_lab3.Util

import ru.itmo.soa_lab3.Exceptions.EjbNotAvailableException
import ru.itmo.soa_lab3.Model.ResponseGroupedDiscount
import ru.itmo.soa_lab3.Model.ResponsePagesTickets
import ru.itmo.soa_lab3.Model.Ticket
import ru.itmo.soa_lab3.Model.TicketType
import ru.itmo.soa_lab3.Service.TicketServiceInterface
import java.util.*
import javax.naming.Context
import javax.naming.InitialContext
import javax.naming.NamingException

class RemoteBeanUtil {
    companion object{
        fun lookupRemoteStatelessBean(): TicketServiceInterface {
            val jndiProperties = Hashtable<String, String>()
            jndiProperties[javax.naming.Context.URL_PKG_PREFIXES] = "org.jboss.ejb.client.naming"
            val contextProperties = Properties()
            contextProperties.setProperty(
                Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.enterprise.naming.SerialInitContextFactory"
            )
            return try {
                val context = InitialContext(contextProperties)
                val appName = "global"
                val moduleName = "soa_lab3-remote-ejb"
                val beanName = "TicketService"
                val viewClassName: String = TicketServiceInterface::class.java.getName()
                val lookupName = "java:$appName/$moduleName/$beanName"
//                val lookupName = "java:global/soa_lab3-remote-ejb/TicketService"
                context.lookup(lookupName) as TicketServiceInterface
            } catch (e: NamingException) {
                object : TicketServiceInterface {
                    override fun getTicketsSortPaging(
                        page: Int,
                        perPage: Int,
                        sortStateList: List<String>?,
                        filterMap: Map<String, MutableList<String>>
                    ): ResponsePagesTickets? {
                        throw EjbNotAvailableException("Не удалось получить доступ к EJB TicketService")
                    }

                    override fun addTicketFromXml(xml: String?): Ticket? {
                        throw EjbNotAvailableException("Не удалось получить доступ к EJB TicketService")
                    }

                    override fun updateTicketFromXml(xml: String?, id: Long) {
                        throw EjbNotAvailableException("Не удалось получить доступ к EJB TicketService")
                    }

                    override fun deleteTicket(ticketId: Long) {
                        throw EjbNotAvailableException("Не удалось получить доступ к EJB TicketService")
                    }

                    override fun getGroupedByDiscount(): MutableList<ResponseGroupedDiscount>? {
                        throw EjbNotAvailableException("Не удалось получить доступ к EJB TicketService")
                    }

                    override fun getDistinctTypes(): MutableList<Any>? {
                        throw EjbNotAvailableException("Не удалось получить доступ к EJB TicketService")
                    }

                    override fun deleteSomeTicketByType(type: TicketType): Long {
                        throw EjbNotAvailableException("Не удалось получить доступ к EJB TicketService")
                    }

                    override fun getTicket(id: Long): Ticket? {
                        throw EjbNotAvailableException("Не удалось получить доступ к EJB TicketService")
                    }
                }
            }
        }
    }
}