package ru.itmo.springsoap

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
open class SpringSoapApplication

fun main(args: Array<String>) {
    runApplication<SpringSoapApplication>(*args)
}
