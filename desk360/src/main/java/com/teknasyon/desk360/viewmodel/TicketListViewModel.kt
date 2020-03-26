package com.teknasyon.desk360.viewmodel

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.model.Desk360Register
import com.teknasyon.desk360.model.Desk360RegisterResponse
import com.teknasyon.desk360.model.Desk360TicketListResponse
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.fragment.Desk360CurrentTicketFragment
import retrofit2.Call
import retrofit2.Response
import java.util.*

open class TicketListViewModel : ViewModel() {

    var ticketSize: MutableLiveData<Int>? = MutableLiveData()
    var ticketList: MutableLiveData<ArrayList<Desk360TicketResponse>>? = MutableLiveData()
    var expiredList: MutableLiveData<ArrayList<Desk360TicketResponse>>? = MutableLiveData()
    var progress: ObservableInt? = null

    init {
        progress = ObservableInt(View.GONE)
        Desk360Constants.getDeviceId()
    }

    fun getTicketList(showLoading: Boolean) {

        if(showLoading) {
            progress?.set(View.VISIBLE)
        }

        Desk360RetrofitFactory.instance.httpService.getTicket().enqueue(object : BaseCallback<Desk360TicketListResponse>() {

            override fun onResponseSuccess(call: Call<Desk360TicketListResponse>, response: Response<Desk360TicketListResponse>) {

                if (response.isSuccessful && response.body() != null) {

                    ticketSize?.value = response.body()?.data?.size

                    val unreadList = response.body()!!.data?.filter { unread -> unread.status == "unread" } as ArrayList<Desk360TicketResponse>

                    RxBus.publish(hashMapOf("sizeTicketList" to response.body()!!.data?.size))
                    RxBus.publish(hashMapOf("unReadSizeTicketList" to unreadList.size))

                    ticketList?.value = response.body()!!.data?.filter { it.status != "expired" } as ArrayList<Desk360TicketResponse>
                    Desk360CurrentTicketFragment.ticketSize = (ticketList?.value as ArrayList<Desk360TicketResponse>).size

                    expiredList?.value = response.body()!!.data?.filter { it.status == "expired" } as ArrayList<Desk360TicketResponse>

                } else {

                    ticketList?.value = null
                }

                progress?.set(View.GONE)
            }

            override fun onFailure(call: Call<Desk360TicketListResponse>, t: Throwable) {
                super.onFailure(call, t)
                progress?.set(View.GONE)
            }
        })
    }

    fun register(showLoading: Boolean) {

        val register = Desk360Register()
        register.app_key = Desk360Constants.app_key
        register.device_id = Desk360Config.instance.getDesk360Preferences()?.adId
        register.app_platform = "Android"
        register.app_version = Desk360Constants.app_version
        register.language_code = Desk360Constants.language_code
        register.time_zone = Desk360Constants.time_zone

        Desk360RetrofitFactory.instance.sslService.register(register).enqueue(object : BaseCallback<Desk360RegisterResponse>() {

            override fun onResponseSuccess(call: Call<Desk360RegisterResponse>, response: Response<Desk360RegisterResponse>) {

                if (response.isSuccessful && response.body() != null) {

                    Desk360Config.instance.getDesk360Preferences()?.data = response.body()!!.data
                    Desk360Config.instance.getDesk360Preferences()?.meta = response.body()!!.meta

                    getTicketList(showLoading)
                }
            }

            override fun onFailure(call: Call<Desk360RegisterResponse>, t: Throwable) {
                super.onFailure(call, t)
                progress?.set(View.GONE)
            }
        })
    }
}