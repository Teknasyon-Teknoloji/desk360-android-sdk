package com.teknasyon.desk360.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.model.Desk360Message
import com.teknasyon.desk360.model.Desk360MessageResponse
import com.teknasyon.desk360.model.Desk360TicketMessage
import com.teknasyon.desk360.model.Desk360TicketResponse
import retrofit2.Call
import retrofit2.Response

open class TicketDetailViewModel(private val ticketId: Int = -1) : ViewModel() {

    var ticketDetailList: MutableLiveData<Desk360TicketResponse>? = MutableLiveData()
    var addMessageItem: MutableLiveData<Desk360Message> = MutableLiveData()

    init {
        getTicketById()
    }

    private fun getTicketById() {

        if (ticketId == -1)
            return

        Desk360RetrofitFactory.instance.desk360Service.getMessages(ticketId = ticketId).enqueue(object : BaseCallback<Desk360TicketMessage>() {

            override fun onResponseSuccess(call: Call<Desk360TicketMessage>, response: Response<Desk360TicketMessage>) {

                if (response.isSuccessful && response.body() != null) {

                    ticketDetailList?.value = response.body()!!.data

                } else {

                    ticketDetailList?.value = null
                }
            }
        })
    }


    fun addMessage(id: Int, message: String) {

        Desk360RetrofitFactory.instance.desk360Service.addMessage(ticketId = id, message = message).enqueue(object : BaseCallback<Desk360MessageResponse>() {

            override fun onResponseSuccess(call: Call<Desk360MessageResponse>, response: Response<Desk360MessageResponse>) {

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
}