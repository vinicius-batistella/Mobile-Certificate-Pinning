package com.example.cert_pinning_final

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

object Retrofit {

    private const val BASE_URL = "https://domain.test/"

    private fun getOkHttpClient(context: Context): OkHttpClient{
        val certificateFactory = CertificateFactory.getInstance("X.509")
        val inputStream: InputStream = context.resources.openRawResource(R.raw.domain_test)
        val certificate = certificateFactory.generateCertificate(inputStream)
        val password = "123456".toCharArray()

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply { load(null, password) }
        keyStore.setCertificateEntry("domain_test", certificate)

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(keyStore)
        }

        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, trustManagerFactory.trustManagers, null)
        }

        val certificatePinner = CertificatePinner.Builder()
            .add("domain.test", "sha256/AFzFoQFk7Ns4MT/Y76RtO+5R+s2g/zSoE/vwBcIoylY=")
            .build()

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManagerFactory.trustManagers[0] as javax.net.ssl.X509TrustManager)
            .certificatePinner(certificatePinner)
            .build()
    }

    fun provideRetrofit(context: Context): Retrofit {

        val okHttpClient = getOkHttpClient(context)

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}