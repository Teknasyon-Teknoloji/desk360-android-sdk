package com.teknasyon.desk360.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.helper.getMediaType
import com.teknasyon.desk360.model.Desk360Message
import com.teknasyon.desk360.model.Desk360MessageResponse
import com.teknasyon.desk360.model.Desk360TicketMessage
import com.teknasyon.desk360.model.Desk360TicketResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class TicketDetailViewModel(private val ticketId: Int = -1) : ViewModel() {

    var ticketDetailList: MutableLiveData<Desk360TicketResponse>? = MutableLiveData()
    var addMessageItem: MutableLiveData<Desk360Message> = MutableLiveData()
    var videoPath: MutableLiveData<String> = MutableLiveData()

    init {
        getTicketById()
    }

    private fun getTicketById() {

        if (ticketId == -1)
            return

        Desk360RetrofitFactory.instance.httpService.getMessages(ticketId)
            .enqueue(object : BaseCallback<Desk360TicketMessage>() {

                override fun onResponseSuccess(
                    call: Call<Desk360TicketMessage>,
                    response: Response<Desk360TicketMessage>
                ) {

                    if (response.isSuccessful && response.body() != null) {

                        ticketDetailList?.value = response.body()!!.data

                    } else {

                        ticketDetailList?.value = null
                    }
                }
            })
    }

    fun addMessage(id: Int, message: String, attachments: List<File>) {
        val msg = message.toRequestBody("text/plain".toMediaTypeOrNull())
        val files = attachments.map { file ->
            MultipartBody.Part.createFormData(
                "attachments[]",
                file.name,
                file.asRequestBody(file.getMediaType())
            )
        }

        Desk360RetrofitFactory.instance.httpService.addMessage(id, msg, files)
            .enqueue(object : BaseCallback<Desk360MessageResponse>() {

                override fun onResponseSuccess(
                    call: Call<Desk360MessageResponse>,
                    response: Response<Desk360MessageResponse>
                ) {
                    val body = response.body()
                    if (response.isSuccessful && body != null) {

                        if (body.meta?.success == true) {
                            addMessageItem.postValue(body.data)
                        }

                    } else {

                        ticketDetailList?.value = null
                    }
                }
            })
    }
}