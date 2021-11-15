package ru.itmo.soa_lab3.Util

import com.orbitz.consul.AgentClient
import com.orbitz.consul.Consul
import com.orbitz.consul.model.agent.ImmutableRegistration
import com.orbitz.consul.model.agent.Registration
import java.io.InputStream
import java.util.*
import javax.annotation.PostConstruct
import javax.ejb.Schedule
import javax.ejb.Singleton
import javax.ejb.Startup

@Singleton
@Startup
open class ConsulUtil {
    private var agentClient: AgentClient? = null
    private var service_id = "1"
    private var port:Int = 8181
    private var name:String? = null
    private var ttl:Long = 3

    @PostConstruct
    fun register(){
        val classLoader = Thread.currentThread().contextClassLoader
        val input: InputStream = classLoader.getResourceAsStream("consul.properties")!!
        val properties = Properties()
        properties.load(input)
        port = properties.getProperty("consul.port").toInt()
        name = properties.getProperty("consul.name")
        service_id = properties.getProperty("consul.service_id")
        ttl = properties.getProperty("consul.ttl").toLong()

        try{
            val consul = Consul.builder().build()
            agentClient = consul!!.agentClient()
            agentClient!!.register(ImmutableRegistration.builder()
                .id(service_id)
                .name("soa-lab3-main")
                .port(8181)
                .check(Registration.RegCheck.ttl(ttl))
                .build())
            println("CONSUL REGISTERED!!!")
        }
        catch (e:Exception){
            println("Error trying to get Consul")
        }
    }
    @Schedule(hour = "*", minute = "*", second = "*/20")
    fun checkIn() {
        agentClient?.pass(service_id)
    }
}