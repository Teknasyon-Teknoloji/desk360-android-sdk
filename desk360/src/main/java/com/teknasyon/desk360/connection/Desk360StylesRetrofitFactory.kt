package com.teknasyon.desk360.connection

import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Desk360StylesRetrofitFactory private constructor() {

    val sslService: SslService
    private var unSecureRetrofitInstance: Retrofit? = null

    init {
        val logging = HttpLoggingInterceptor()

        logging.level = HttpLoggingInterceptor.Level.BODY

        val timeoutInterval = 60
        val httpClientWithHeader = OkHttpClient.Builder()


        val httpClientWithoutHeader = OkHttpClient.Builder()
        httpClientWithoutHeader.addInterceptor(logging)
        httpClientWithoutHeader.connectTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)
        httpClientWithoutHeader.readTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)

        httpClientWithHeader.addInterceptor { chain ->
            var request = chain.request()
            request = request.newBuilder().apply {
                Desk360Config.instance.getDesk360Preferences()?.data?.access_token.let {
                    addHeader("Authorization", "Bearer $it")
                }
            }.build()
            chain.proceed(request)
        }

        val client = httpClientWithHeader.build()

        if (unSecureRetrofitInstance == null)
            unSecureRetrofitInstance = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(Desk360Constants.baseURL!!)
                .build()

        sslService = unSecureRetrofitInstance!!.create(SslService::class.java)
    }

    companion object {

        private var INSTANCE: Desk360StylesRetrofitFactory? = null

        val instance: Desk360StylesRetrofitFactory

            get() {
                if (INSTANCE == null) {
                    INSTANCE = Desk360StylesRetrofitFactory()
                }
                return INSTANCE!!
            }
    }
}
