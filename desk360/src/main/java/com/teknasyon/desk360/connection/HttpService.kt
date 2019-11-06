package com.teknasyon.desk360.connection

import com.teknasyon.desk360.model.*
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import retrofit2.Call
import retrofit2.http.*

interface HttpService {
    @POST("api/v1/tickets")
    fun addTicket(@Body ticketItem: Desk360TicketReq): Call<Desk360NewSupportResponse>

    @GET("api/v1/tickets")
    fun getTicket(): Call<Desk360TicketListResponse>


    @GET("api/v1/sdk")
    fun getTypes(): Call<Desk360ConfigResponse>

    @GET("api/v1/tickets/{ticket_id}")
    fun getMessages(@Path("ticket_id") ticket_id: Int): Call<Desk360TickeMessage>

    @POST("api/v1/tickets/{ticket_id}/messages")
    fun addMessage(@Path("ticket_id") ticket_id: Int, @Query("message") message: String): Call<Desk360MessageResponse>

    @GET("api/v1/tickets/types/list")
    fun getTypeList(): Call<Desk360TypeResponse>
}
