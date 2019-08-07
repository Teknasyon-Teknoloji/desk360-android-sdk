package com.teknasyon.desk360.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.model.Register
import com.teknasyon.desk360.model.RegisterResponse
import com.teknasyon.desk360.model.TicketListResponse
import com.teknasyon.desk360.model.TicketResponse
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.*

/**
 * Created by seyfullah on 24,May,2019
 *
 */

open class TicketListViewModel : ViewModel() {
    var ticketList: MutableLiveData<ArrayList<TicketResponse>>? = MutableLiveData()

    init {
        Desk360Constants.getDeviceId()
        GlobalScope.launch {
            delay(300)
            register()
        }
    }

    fun getTicketList() {
        Desk360RetrofitFactory.instance.httpService.getTicket()
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
        register.app_key = Desk360Constants.app_key
        register.device_id = Desk360Config.instance.getDesk360Preferences()?.adId
        register.app_platform = "Android"
        register.app_version = Desk360Constants.app_version
        register.language_code = Desk360Constants.language_code
        register.time_zone = Desk360Constants.time_zone

        Desk360RetrofitFactory.instance.sslService.register(register)
            .enqueue(object : BaseCallback<RegisterResponse>() {
                override fun onResponseSuccess(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        Desk360Config.instance.getDesk360Preferences()?.data = response.body()!!.data
                        Desk360Config.instance.getDesk360Preferences()?.meta = response.body()!!.meta
                        getTicketList()
                    }
                }
            })
    }
}