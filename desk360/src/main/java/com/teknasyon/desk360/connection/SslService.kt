package com.teknasyon.desk360.connection


import com.teknasyon.desk360.model.Desk360Register
import com.teknasyon.desk360.model.Desk360RegisterResponse
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * This interface is intended to use apis which need secure connection
 */
interface SslService {

    @POST("api/v1/devices/register")
    fun register(@Body register: Desk360Register): Call<Desk360RegisterResponse>

    @POST("api/v1/sdk/configurations")
    fun getTypes(@Body language_code: HashMap<String, String?>): Call<Desk360ConfigResponse>

}
