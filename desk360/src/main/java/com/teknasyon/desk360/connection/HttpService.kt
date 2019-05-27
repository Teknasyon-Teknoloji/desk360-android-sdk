package com.teknasyon.desk360.connection

import com.teknasyon.desk360.model.TicketListResponse
import com.teknasyon.desk360.model.TicketResponce
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface HttpService {
    @POST("api/v1/tickets")
    fun addTicket(@Body ticketResponce: TicketResponce): Call<Any>

    @GET("api/v1/tickets")
    fun getTicket(): Call<TicketListResponse>

    @GET("api/v1/tickets/{ticket_id}")
    fun getMessages(@Path("ticket_id") ticket_id: Int): Call<Map<String, String>>

    @POST("api/v1/tickets/{ticket_id}/messages")
    fun addMessage(@Path("ticket_id") ticket_id: Int, @Body message: String): Call<Any>

    @GET("api/v1/tickets/types/list")
    fun getTypeList(): Call<Any>
}
