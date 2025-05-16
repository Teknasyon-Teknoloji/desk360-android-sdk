package com.teknasyon.desk360.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360SDK
import com.teknasyon.desk360.helper.Platform
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.model.Desk360Register
import com.teknasyon.desk360.model.Desk360RegisterResponse
import com.teknasyon.desk360.model.Desk360TicketListResponse
import com.teknasyon.desk360.model.Desk360TicketResponse
import retrofit2.Call
import retrofit2.Response

open class TicketListViewModel : ViewModel() {

    val ticketSize: MutableLiveData<Int> = MutableLiveData()
    val ticketList: MutableLiveData<List<Desk360TicketResponse>> = MutableLiveData()
    val expiredList: MutableLiveData<List<Desk360TicketResponse>> = MutableLiveData()
    val progress = MutableLiveData(View.GONE)

    init {
        Desk360SDK.getDeviceId()
    }

    fun getTicketList(showLoading: Boolean) {

        if(showLoading) {
            progress.value = View.VISIBLE
        }

        Desk360RetrofitFactory.instance.desk360Service.getTicket().enqueue(object : BaseCallback<Desk360TicketListResponse>() {

            override fun onResponseSuccess(call: Call<Desk360TicketListResponse>, response: Response<Desk360TicketListResponse>) {

                if (response.isSuccessful && response.body() != null) {

                    ticketSize.value = response.body()?.data?.size

                    val unreadList = response.body()!!.data?.filter { unread -> unread.status == "unread" } as ArrayList<Desk360TicketResponse>

                    RxBus.publish(hashMapOf("sizeTicketList" to response.body()!!.data?.size))
                    RxBus.publish(hashMapOf("unReadSizeTicketList" to unreadList.size))

                    ticketList.value =
                        response.body()?.data?.filter { it.status != "expired" } as ArrayList<Desk360TicketResponse>
                    expiredList.value =
                        response.body()?.data?.filter { it.status == "expired" } as ArrayList<Desk360TicketResponse>

                } else {

                    ticketList.value = listOf()
                }

                progress.value = View.GONE
            }

            override fun onFailure(call: Call<Desk360TicketListResponse>, t: Throwable) {
                super.onFailure(call, t)
                progress.value = View.GONE
            }
        })
    }

    fun register(showLoading: Boolean) {
        val register = Desk360Register()
        register.app_key = Desk360SDK.manager?.appKey
        register.device_id = Desk360Config.instance.getDesk360Preferences()?.adId
        register.app_platform =
            if (Desk360SDK.manager?.platform == Platform.HUAWEI) "Huawei" else "Android"
        register.app_version = Desk360SDK.manager?.appVersion
        register.language_code = Desk360SDK.manager?.languageCode

        Desk360RetrofitFactory.instance.desk360Service.register(register).enqueue(object : BaseCallback<Desk360RegisterResponse>() {

            override fun onResponseSuccess(call: Call<Desk360RegisterResponse>, response: Response<Desk360RegisterResponse>) {

                if (response.isSuccessful && response.body() != null) {

                    Desk360Config.instance.getDesk360Preferences()?.data = response.body()!!.data
                    Desk360Config.instance.getDesk360Preferences()?.meta = response.body()!!.meta

                    getTicketList(showLoading)
                }
            }

            override fun onFailure(call: Call<Desk360RegisterResponse>, t: Throwable) {
                super.onFailure(call, t)
                progress.value = View.GONE
            }
        })
    }
}