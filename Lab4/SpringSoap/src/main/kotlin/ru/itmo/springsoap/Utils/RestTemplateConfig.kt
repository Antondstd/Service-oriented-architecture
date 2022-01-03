package ru.itmo.springsoap.Utils

import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.io.File
import java.nio.charset.StandardCharsets
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException


@Configuration
class RestTemplateConfig {
    @Value("\${dir-to-certificates}")
     lateinit var dirToSertificates: String

    @Value("\${custom-trust-store}")
    lateinit var customTrustStore: String

    @Value("\${custom-trust-store-password}")
    lateinit var customTrustStorePassword: String


    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        val sslContext = SSLContextBuilder.create()
            .loadTrustMaterial(File(dirToSertificates + customTrustStore),
                customTrustStorePassword.toCharArray())
            .build()
        val httpClient = HttpClients.custom()
            .setSSLContext(sslContext)
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .build()
        val customRequestFactory = HttpComponentsClientHttpRequestFactory()
        customRequestFactory.httpClient = httpClient
        return builder.requestFactory { customRequestFactory }.errorHandler(RestTemplateResponseErrorHandler())
            .messageConverters(
                StringHttpMessageConverter(
                    StandardCharsets.UTF_8
                )
            ).build()
    }
}