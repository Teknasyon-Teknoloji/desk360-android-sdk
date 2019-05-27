package com.teknasyon.desk360.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.Desc360Application
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.RetrofitFactory
import com.teknasyon.desk360.model.Register
import com.teknasyon.desk360.model.RegisterResponse
import com.teknasyon.desk360.model.TicketListResponse
import com.teknasyon.desk360.model.TicketResponce
import retrofit2.Call
import retrofit2.Response

/**
 * Created by seyfullah on 24,May,2019
 *
 */

class TicketListViewModel : ViewModel() {
    var ticketList: MutableLiveData<ArrayList<TicketResponce>>? = MutableLiveData()

    init {
        register()
    }

    fun getTicketList() {
        RetrofitFactory.instance.httpService.getTicket()
            .enqueue(object : BaseCallback<TicketListResponse>() {
                override fun onResponseSuccess(
                    call: Call<TicketListResponse>,
                    response: Response<TicketListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        ticketList?.value = response.body()!!.data
                    } else {
                        ticketList?.value = null
                    }
                }
            })
    }

    private fun register() {
        val register = Register()
        register.app_key = "123456"
        register.device_id = "desk360-001"
        RetrofitFactory.instance.sslService.register(register)
            .enqueue(object : BaseCallback<RegisterResponse>() {
                override fun onResponseSuccess(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Desc360Application.instance.getAresPreferences()?.data = response.body()!!.data
                        Desc360Application.instance.getAresPreferences()?.meta = response.body()!!.meta
                        getTicketList()
                    }
                }
            })
    }
}