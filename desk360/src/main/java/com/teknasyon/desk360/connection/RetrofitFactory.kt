package com.teknasyon.desk360.connection

import com.teknasyon.desk360.BuildConfig
import com.teknasyon.desk360.Desc360Application
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitFactory private constructor() {

    val sslService: SslService
    private var secureRetrofitInstance: Retrofit? = null

    val httpService: HttpService
    private var unSecureRetrofitInstance: Retrofit? = null

    init {
        val logging = HttpLoggingInterceptor()

        logging.level = HttpLoggingInterceptor.Level.NONE

        val timeoutInterval = 60
        val httpClientWithHeader = OkHttpClient.Builder()
        httpClientWithHeader.addInterceptor(logging)
        httpClientWithHeader.connectTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)
        httpClientWithHeader.readTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)

        httpClientWithHeader.addInterceptor { chain ->
            var request = chain.request()
            request = request.newBuilder().apply {
                Desc360Application.instance.getAresPreferences()?.data?.access_token.let {
                    addHeader("Authorization", "Bearer $it")
                }
            }.build()
            chain.proceed(request)
        }

        val httpClientWithoutHeader = OkHttpClient.Builder()
        httpClientWithoutHeader.addInterceptor(logging)
        httpClientWithoutHeader.connectTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)
        httpClientWithoutHeader.readTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)

        val client = httpClientWithHeader.build()
        if (unSecureRetrofitInstance == null)
            unSecureRetrofitInstance = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(BuildConfig.SSL_BASE_URL)
                .build()

        val client1 = httpClientWithoutHeader.build()
        if (secureRetrofitInstance == null)
            secureRetrofitInstance = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client1)
                .baseUrl(BuildConfig.SSL_BASE_URL)
                .build()

        sslService = secureRetrofitInstance!!.create(SslService::class.java)
        httpService = unSecureRetrofitInstance!!.create(HttpService::class.java)
    }

    companion object {
        private var INSTANCE: RetrofitFactory? = null
        val instance: RetrofitFactory
            get() {
                if (INSTANCE == null) {
                    INSTANCE = RetrofitFactory()
                }
                return INSTANCE!!
            }
    }
}
