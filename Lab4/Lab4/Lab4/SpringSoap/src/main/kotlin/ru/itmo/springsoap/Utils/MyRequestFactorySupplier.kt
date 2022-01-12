package ru.itmo.springsoap.Utils

import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.logging.log4j.util.Supplier
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory


class MyRequestFactorySupplier : Supplier<ClientHttpRequestFactory> {
    override fun get(): ClientHttpRequestFactory {

        var httpClient1: CloseableHttpClient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier()).build()

        var requestFactory: ClientHttpRequestFactory = HttpComponentsClientHttpRequestFactory().apply { httpClient = httpClient1 }
        return requestFactory
    }
}