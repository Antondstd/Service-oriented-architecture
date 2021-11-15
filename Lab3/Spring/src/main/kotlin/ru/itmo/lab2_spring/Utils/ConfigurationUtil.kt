package ru.itmo.lab2_spring.Utils

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter


@Configuration
class ConfigurationUtil {
//    @Value("\${apiService.url}")
//    lateinit var urlApiService: String

    @Value("\${consul.main-app-name}")
    lateinit var mainAppName: String

    @Autowired
    lateinit var discoveryClient: DiscoveryClient

     private var serviceInstance:ServiceInstance? = null

    fun getServiceInstance(): ServiceInstance {
        return (discoveryClient!!.getInstances(mainAppName)
            .stream()
            .findFirst()).get()
    }

    fun urlApiService():String{
        if (serviceInstance == null){
            serviceInstance = getServiceInstance()
        }
        return "https://${serviceInstance!!.host}:${serviceInstance!!.port}/api"
    }


    @Bean
    fun corsConfigurer(): WebMvcConfigurer? {
        return object : WebMvcConfigurerAdapter() {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
            }
        }
    }
}