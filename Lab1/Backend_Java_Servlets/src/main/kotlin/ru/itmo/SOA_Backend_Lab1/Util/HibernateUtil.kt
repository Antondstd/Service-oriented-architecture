package ru.itmo.SOA_Backend_Lab1.Util

import ru.itmo.SOA_Backend_Lab1.Model.Coordinates
import ru.itmo.SOA_Backend_Lab1.Model.Event
import ru.itmo.SOA_Backend_Lab1.Model.Ticket
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.hibernate.cfg.Environment
import org.hibernate.service.ServiceRegistry
import java.util.*


class HibernateUtil {

    companion object {
        var sessionFactory: SessionFactory? = null
        get() {
            if (field == null) {
                try {
                    val configuration = Configuration()
                    configuration.apply {
                        addAnnotatedClass(Ticket::class.java)
                        addAnnotatedClass(Coordinates::class.java)
                        addAnnotatedClass(Event::class.java)
                    }
                    val serviceRegistry: ServiceRegistry = StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build()

                    println("Hibernate serviceRegistry created")

                    field = configuration.buildSessionFactory(serviceRegistry)
                    return field
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return field
        }
    }
}