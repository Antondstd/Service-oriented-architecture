package ru.itmo.springsoap.configurations

import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.ws.config.annotation.EnableWs
import org.springframework.ws.config.annotation.WsConfigurerAdapter
import org.springframework.ws.transport.http.MessageDispatcherServlet
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition
import org.springframework.xml.xsd.SimpleXsdSchema
import org.springframework.xml.xsd.XsdSchema


@EnableWs
@Configuration
class WebServiceConfig : WsConfigurerAdapter() { // bean definitions
    @Bean
    fun messageDispatcherServlet(applicationContext: ApplicationContext): ServletRegistrationBean<*> {
        val servlet1: MessageDispatcherServlet = MessageDispatcherServlet()
        servlet1.setApplicationContext(applicationContext)
        servlet1.isTransformWsdlLocations = true
        return ServletRegistrationBean<MessageDispatcherServlet>(servlet1,*arrayOf("/ws/*"))
    }

    @Bean(name = ["tickets"])
    fun defaultWsdl11Definition(ticketsSchema: XsdSchema?): DefaultWsdl11Definition? {
        val wsdl11Definition = DefaultWsdl11Definition()
        wsdl11Definition.setPortTypeName("TicketsPort")
        wsdl11Definition.setLocationUri("/ws")
        wsdl11Definition.setTargetNamespace("http://www.itmo.ru/springsoap/generated")
        wsdl11Definition.setSchema(ticketsSchema)
        return wsdl11Definition
    }

    @Bean
    fun ticketsSchema(): XsdSchema? {
        return SimpleXsdSchema(ClassPathResource("tickets.xsd"))
    }
}