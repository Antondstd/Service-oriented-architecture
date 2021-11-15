package ru.itmo.soa_lab3.DAO

import org.hibernate.Transaction
import ru.itmo.soa_lab3.Exceptions.BadRequestException
import ru.itmo.soa_lab3.Exceptions.NotFoundException
import ru.itmo.soa_lab3.Model.*
import ru.itmo.soa_lab3.Util.HibernateUtil
import java.io.Serializable
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.NoResultException
import javax.persistence.PersistenceException
import javax.persistence.criteria.*


class TicketDAO:Serializable {

    fun getSortedFilteredTickets(
        page: Int,
        perPage: Int,
        sortAdditions: List<QueryAdditions>,
        filterAdditions: List<QueryAdditions>
    ): Pair<List<Ticket>?,String?> {
        var transaction: Transaction? = null
        try {
            val session = HibernateUtil.sessionFactory?.openSession()
            val criteriaBuilder = session?.criteriaBuilder!!
            val q = criteriaBuilder.createQuery(Ticket::class.java)
            val root = q.from(Ticket::class.java)
            var res = q.select(root)
            if (sortAdditions.size > 0) {
                addClause(sortAdditions, root, true, q, criteriaBuilder)
            }
            if (filterAdditions.size > 0) {
                addClause(filterAdditions, root, false, q, criteriaBuilder)
            }

            return Pair(session.createQuery(res).setFirstResult((page - 1) * perPage).setMaxResults(perPage).resultList, null)
        } catch (e: Exception) {
            if (transaction != null)
                transaction.rollback()
            e.printStackTrace()
            return Pair(null,e.cause.toString())
        }
    }

    fun addClause(
        clause: List<QueryAdditions>,
        root: Root<Ticket>,
        type: Boolean,
        resultQuery: CriteriaQuery<*>,
        criteriaBuilder: CriteriaBuilder
    ) {
        val coordinatesJoin: Join<Ticket, Coordinates> = root.join("coordinates")
        val eventJoin: Join<Ticket, Event> = root.join("event")
        val orders = mutableListOf<Order>()
        val conditions: MutableList<Predicate?> = mutableListOf()

        clause.forEach { addition ->

                val cur: Path<String>
                cur = when (addition.table) {
                    TablesQueryAdditions.TICKET -> root.get<String>(addition.column)
                    TablesQueryAdditions.COORDINATES -> coordinatesJoin.get<String>(addition.column)
                    TablesQueryAdditions.EVENT -> eventJoin.get<String>(addition.column)
                    else -> throw BadRequestException("${addition.table} не соответствует ни одному названию таблиц")
                }
            try {
                if (type) {
                    if (addition.sortType)
                        orders.add(criteriaBuilder.asc(cur))
                    else (addition.sortType)
                    orders.add(criteriaBuilder.desc(cur))

                } else {
                    if (addition.column.equals("date") || addition.column.equals("creationDate")) {
                        val formatter:DateTimeFormatter
                        if (addition.column.equals("date")) {
                            formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yy")
                            val date = LocalDateTime.parse(addition.firstValue, formatter)
                            conditions.add(criteriaBuilder.equal(cur, date))
                        }
                        else if (addition.column.equals("creationDate"))
                        {
                            formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yy")
//                            if (addition.firstValue!!.get(17) == ' ')
                            val date = ZonedDateTime.parse(addition.firstValue?.replace(' ','+',true))
                            conditions.add(criteriaBuilder.equal(cur, date))
                        }
                    } else
                        conditions.add(criteriaBuilder.equal(cur, addition.firstValue))
                }
            }
            catch (e:Exception){
                e.printStackTrace()
                throw BadRequestException("${addition.firstValue} -- неправильный формат данных для столбца ${addition.column} таблицы ${addition.table} ")
            }
        }
        if (type)
            resultQuery.orderBy(orders)
        else
            resultQuery.where(criteriaBuilder.and(*conditions.toTypedArray()))
    }

    fun getCountTicket(filterAdditions: List<QueryAdditions>?): Long {
        var transaction: Transaction? = null
            val session = HibernateUtil.sessionFactory?.openSession()
            transaction = session?.beginTransaction()
            if (transaction == null || session == null)
                throw Exception("Не удалось создать транзакцию. Нет сессии с базой данных.")
            val criteriaBuilder = session.criteriaBuilder!!
            val createQueryCount = criteriaBuilder.createQuery(Long::class.java)
            val root = createQueryCount.from(Ticket::class.java)
            if (filterAdditions != null && filterAdditions.size > 0) {
                addClause(filterAdditions, root, false, createQueryCount, criteriaBuilder)
            }
            createQueryCount.select(criteriaBuilder.count(root))
            try {
                val count = session.createQuery(createQueryCount).singleResult
                transaction.commit()
                return count
            }
            catch (e:Exception){
                throw BadRequestException(e.cause.toString())
            }
    }

    fun getTicket(id: Long): Ticket? {
        var transaction: Transaction? = null
        try {
            val session = HibernateUtil.sessionFactory?.openSession()
            transaction = session?.beginTransaction()
            if (transaction == null || session == null)
                return null
            val criteriaBuilder = session.criteriaBuilder!!
            val createQueryTicket = criteriaBuilder.createQuery(Ticket::class.java)
            val root = createQueryTicket.from(Ticket::class.java)
            val ticket = session.createQuery(createQueryTicket.where(criteriaBuilder.equal(root.get<String>("id"),id))).singleResult
            transaction.commit()
            return ticket
        }
        catch (e:NoResultException){
            throw NotFoundException("Не удалось найти Ticket с id $id")
        }
        catch (e: Exception) {
            if (transaction != null)
                transaction.rollback()
            e.printStackTrace()
            return null
        }
    }

    fun updateTicket(ticket: Ticket): String? {
        var transaction: Transaction? = null
        try {
            val session = HibernateUtil.sessionFactory?.openSession()
            transaction = session?.beginTransaction()
            if (transaction == null || session == null)
                return "Не удалось создать транзакцию"
            session.update(ticket)
            transaction.commit()
            return null
        } catch (e: Exception) {
            if (transaction != null)
                transaction.rollback()
            e.printStackTrace()
            return e.cause.toString()
        }
    }

    fun addTicket(ticket: Ticket):Ticket? {
        var transaction: Transaction? = null
        try {
            val session = HibernateUtil.sessionFactory?.openSession()
            transaction = session?.beginTransaction()
            if (transaction == null || session == null)
                return null
            session.save(ticket)
            transaction.commit()
            session
            return ticket
        }
        catch (e:PersistenceException){
            throw BadRequestException(e.cause.toString())
        }
        catch (e: Exception) {
            if (transaction != null)
                transaction.rollback()
            e.printStackTrace()
            return null
        }
    }

     fun deleteTicket(ticketId: Long) {
        var transaction: Transaction? = null
        try {
            val session = HibernateUtil.sessionFactory?.openSession()
            transaction = session?.beginTransaction()
            if (transaction == null || session == null)
                throw Exception("Не удалось создать транзакцию. Нет сессии с базой данных.")
            val ticket = session.find(Ticket::class.java, ticketId)
            if (ticket != null) {
                session.delete(ticket)
                session.flush()
                transaction.commit()
            }
            else{
                transaction.rollback()
                throw NotFoundException("Не был найден Ticket с id: $ticketId")
            }
        }
        catch (e:NotFoundException){
            throw NotFoundException("Не был найден Ticket с id: $ticketId")

        }
        catch (e: Exception) {
            if (transaction != null)
                transaction.rollback()
            e.printStackTrace()
        }
    }

     fun getGroupedByDiscount(): MutableList<ResponseGroupedDiscount>? {
        var transaction: Transaction? = null
        try {
            val session = HibernateUtil.sessionFactory?.openSession()
            transaction = session?.beginTransaction()
            if (transaction == null || session == null)
                throw Exception("Не удалось создать транзакцию. Нет сессии с базой данных.")
            val criteriaBuilder = session.criteriaBuilder!!
            val criteriaQuery = criteriaBuilder.createQuery(ResponseGroupedDiscount::class.java)
            val root = criteriaQuery.from(Ticket::class.java)
            criteriaQuery.groupBy(root.get<String>("discount"));
            val query = criteriaQuery.multiselect(root.get<String>("discount"),criteriaBuilder.count(root))
            val list = session.createQuery(query).resultList
            session.close()
            return list
        } catch (e: Exception) {
            if (transaction != null)
                transaction.rollback()
            e.printStackTrace()
            return null
        }
    }

     fun getDistinctTypes(): MutableList<Any>? {
        var transaction: Transaction? = null
        try {
            val session = HibernateUtil.sessionFactory?.openSession()
            transaction = session?.beginTransaction()
            if (transaction == null || session == null)
                return null
            val criteriaBuilder = session.criteriaBuilder!!
            val criteriaQuery = criteriaBuilder.createQuery()
            val root = criteriaQuery.from(Ticket::class.java)
            val query = criteriaQuery.multiselect(root.get<String>("type")).distinct(true);
            return session.createQuery(query).resultList
        } catch (e: Exception) {
            if (transaction != null)
                transaction.rollback()
            e.printStackTrace()
            return null
        }
    }

     fun deleteSomeTicketByType(type: TicketType): Long {
        var transaction: Transaction? = null
        try {
            val session = HibernateUtil.sessionFactory?.openSession()
            transaction = session?.beginTransaction()
            if (transaction == null || session == null)
                throw Exception("Не удалось создать транзакцию. Нет сессии с базой данных.")
            val criteriaBuilder = session.criteriaBuilder!!
            val criteriaQuery = criteriaBuilder.createQuery(Ticket::class.java)
            val root = criteriaQuery.from(Ticket::class.java)
            val query = criteriaQuery.where(criteriaBuilder.equal(root.get<String>("type"), type))
            val ticket = session.createQuery(query).setMaxResults(1).singleResult
            if (ticket != null) {
                session.delete(ticket)
                session.flush()
            }
            transaction.commit()
            return ticket.id
        }
        catch (e:NoResultException){
            throw NotFoundException("Не удалось найти ни одни Ticket с типом $type")
        }
        catch (e: Exception) {
            if (transaction != null)
                transaction.rollback()
            e.printStackTrace()
            return -1
        }
    }
}