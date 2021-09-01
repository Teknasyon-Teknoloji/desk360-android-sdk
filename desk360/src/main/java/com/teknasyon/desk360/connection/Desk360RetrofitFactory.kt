package com.teknasyon.desk360.connection

import com.teknasyon.desk360.BuildConfig
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Environment
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Desk360RetrofitFactory private constructor() {

    val desk360Service: Desk360Service
    private var retrofit: Retrofit? = null

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val timeoutInterval = 60
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeoutInterval.toLong(), TimeUnit.SECONDS)
            .addInterceptor { chain ->
                var request = chain.request()
                request = request.newBuilder().apply {
                    addHeader("Version", BuildConfig.VERSION_NAME)
                    addHeader("Environment", Environment.PRODUCTION)
                }.build()
                chain.proceed(request)
            }
            .build()

        if (retrofit == null)
            retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(Desk360Constants.baseURL)
                .build()

        desk360Service = retrofit!!.create(Desk360Service::class.java)
    }

    companion object {
        private var INSTANCE: Desk360RetrofitFactory? = null
        val instance: Desk360RetrofitFactory
            get() {
                if (INSTANCE == null) {
                    INSTANCE = Desk360RetrofitFactory()
                }
                return INSTANCE!!
            }
    }
}
