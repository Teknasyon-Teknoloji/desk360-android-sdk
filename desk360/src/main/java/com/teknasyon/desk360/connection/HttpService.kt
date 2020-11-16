package com.teknasyon.desk360.connection

import com.teknasyon.desk360.model.*
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface HttpService {

    @Multipart
    @POST("api/v1/tickets")
    fun addTicket(
        @PartMap ticketItem: HashMap<String, RequestBody>,
        @Part attachment: MultipartBody.Part?
    ): Call<Desk360NewSupportResponse>

    @GET("api/v1/tickets")
    fun getTicket(): Call<Desk360TicketListResponse>

    @GET("api/v1/tickets/{ticket_id}")
    fun getMessages(@Path("ticket_id") ticket_id: Int): Call<Desk360TicketMessage>

    @Multipart
    @POST("api/v1/tickets/{ticket_id}/messages")
    fun addMessage(
        @Path("ticket_id") ticket_id: Int,
        @Part("message") message: RequestBody,
        @Part attachments: List<MultipartBody.Part>
    ): Call<Desk360MessageResponse>

    @GET("api/v1/tickets/types/list")
    fun getTypeList(): Call<Desk360TypeResponse>

    @POST("api/v1/sdk/configurations")
    fun getTypes(@Body language_code: HashMap<String, String?>): Call<Desk360ConfigResponse>

    @GET
    fun download(@Url fileUrl: String): Call<ResponseBody>
}

