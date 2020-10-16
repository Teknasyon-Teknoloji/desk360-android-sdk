package com.teknasyon.desk360.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.connection.HttpService
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.WriteVideoToDisk
import com.teknasyon.desk360.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

open class TicketDetailViewModel(private val ticketId: Int = -1) : ViewModel() {

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
            .enqueue(object : BaseCallback<Desk360TickeMessage>() {

                override fun onResponseSuccess(
                    call: Call<Desk360TickeMessage>,
                    response: Response<Desk360TickeMessage>
                ) {

                    if (response.isSuccessful && response.body() != null) {

                        ticketDetailList?.value = response.body()!!.data

                    } else {

                        ticketDetailList?.value = null
                    }
                }
            })
    }

    fun addMessage(id: Int, message: String) {

        Desk360RetrofitFactory.instance.httpService.addMessage(id, message)
            .enqueue(object : BaseCallback<Desk360MessageResponse>() {

                override fun onResponseSuccess(
                    call: Call<Desk360MessageResponse>,
                    response: Response<Desk360MessageResponse>
                ) {

                    if (response.isSuccessful && response.body() != null) {

                        if (response.body()!!.meta?.success == true) {
                            addMessageItem.postValue(response.body()!!.data)
                        }

                    } else {

                        ticketDetailList?.value = null
                    }
                }
            })
    }

    fun downloadFile(path: String, fileDownload: HttpService, desk360File: Desk360File) {

        fileDownload.download(desk360File.url)
            .enqueue(object : BaseCallback<ResponseBody>() {

                override fun onResponseSuccess(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                    response.body()?.let {

                        val writeResponseToDisk = WriteVideoToDisk()

                        val thread = Thread(Runnable {

                            writeResponseToDisk.writeResponseBodyToDisk(
                                path,
                                response.body(),
                                desk360File.name
                            )
                            videoPath.postValue(path + desk360File.name)
                        })

                        thread.start()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    super.onFailure(call, t)
                }
            })
    }
}