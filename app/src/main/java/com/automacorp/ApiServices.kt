package com.automacorp

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

object ApiServices {
    private const val API_USERNAME = "user"
    private const val API_PASSWORD = "password"
    private const val BASE_URL = "https://automacorp.devmind.cleverapps.io/api/"

    class BasicAuthInterceptor(
        private val username: String,
        private val password: String
    ) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val request = chain.request().newBuilder()
                .header("Authorization", Credentials.basic(username, password))
                .build()
            return chain.proceed(request)
        }
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        val trustManager = object : X509TrustManager {
            @Throws(CertificateException::class)
            override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}

            @Throws(CertificateException::class)
            override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}

            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        }

        val sslContext = SSLContext.getInstance("SSL").apply {
            init(null, arrayOf(trustManager), SecureRandom())
        }

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustManager)
            .hostnameVerifier { hostname, _ -> hostname.contains("cleverapps.io") }
            .addInterceptor(BasicAuthInterceptor(API_USERNAME, API_PASSWORD))
    }

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    val roomsApiService: RoomsApiService by lazy {
        val client = getUnsafeOkHttpClient().build()

        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(RoomsApiService::class.java)
    }
}