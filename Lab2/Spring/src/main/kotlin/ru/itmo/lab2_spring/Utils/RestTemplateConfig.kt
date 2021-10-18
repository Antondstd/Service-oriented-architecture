package ru.itmo.lab2_spring.Utils

import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
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
    @Bean
    @Throws(NoSuchAlgorithmException::class, KeyManagementException::class)
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
//        val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
//            object : X509TrustManager {
//
//
//                override fun checkClientTrusted(
//                    certs: Array<X509Certificate?>?, authType: String?
//                ) {
//                }
//
//                override fun checkServerTrusted(
//                    certs: Array<X509Certificate?>?, authType: String?
//                ) {
//                }
//
//                override fun getAcceptedIssuers(): Array<X509Certificate> {
//                    return arrayOf<X509Certificate>()
//                }
//            }
//        )
//        val sslContext: SSLContext = SSLContext.getInstance("SSL")
//        sslContext.init(null, trustAllCerts, SecureRandom())
//        val httpClient = HttpClients.custom()
//            .setSSLContext(sslContext)
//            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
//            .build()
        val sslContext = SSLContextBuilder.create()
            .loadTrustMaterial(File("C:\\Program Files\\Java\\jdk-11.0.11\\bin\\payaratospringtruststore.jks"),
                "soasoa".toCharArray())
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