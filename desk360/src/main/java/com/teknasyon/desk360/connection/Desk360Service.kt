package com.teknasyon.desk360.connection

import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.model.*
import com.teknasyon.desk360.modelv2.Desk360ConfigRequestModel
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface Desk360Service {
    @POST("api/v1/devices/register")
    fun register(@Body register: Desk360Register): Call<Desk360RegisterResponse>

    @POST("api/v1/sdk/configurations")
    fun getTypes(
        @Header("Authorization") token: String = "Bearer ${Desk360Config.instance.getDesk360Preferences()?.data?.access_token}",
        @Body requestModel: Desk360ConfigRequestModel
    ): Call<Desk360ConfigResponse>

    @Multipart
    @POST("api/v1/tickets")
    fun addTicket(
        @Header("Authorization") token: String = "Bearer ${Desk360Config.instance.getDesk360Preferences()?.data?.access_token}",
        @PartMap ticketItem: HashMap<String, RequestBody>,
        @Part attachment: MultipartBody.Part?
    ): Call<Desk360NewSupportResponse>

    @GET("api/v1/tickets")
    fun getTicket(@Header("Authorization") token: String = "Bearer ${Desk360Config.instance.getDesk360Preferences()?.data?.access_token}"): Call<Desk360TicketListResponse>

    @GET("api/v1/tickets/{ticket_id}")
    fun getMessages(
        @Header("Authorization") token: String = "Bearer ${Desk360Config.instance.getDesk360Preferences()?.data?.access_token}",
        @Path("ticket_id") ticket_id: Int
    ): Call<Desk360TickeMessage>

    @POST("api/v1/tickets/{ticket_id}/messages")
    fun addMessage(
        @Header("Authorization") token: String = "Bearer ${Desk360Config.instance.getDesk360Preferences()?.data?.access_token}",
        @Path("ticket_id") ticket_id: Int,
        @Query("message") message: String
    ): Call<Desk360MessageResponse>
}

