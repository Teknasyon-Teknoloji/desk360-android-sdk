package com.teknasyon.desk360.viewmodel

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.RxBus
import com.teknasyon.desk360.helper.register
import com.teknasyon.desk360.model.Desk360TicketListResponse
import com.teknasyon.desk360.model.Desk360TicketResponse
import com.teknasyon.desk360.view.fragment.Desk360CurrentTicketFragment
import retrofit2.Call
import retrofit2.Response
import java.util.*

class TicketListViewModel : ViewModel() {

    var ticketSize: MutableLiveData<Int>? = MutableLiveData()
    var ticketList: MutableLiveData<ArrayList<Desk360TicketResponse>>? = MutableLiveData()
    var expiredList: MutableLiveData<ArrayList<Desk360TicketResponse>>? = MutableLiveData()
    var progress: ObservableInt? = null

    init {
        progress = ObservableInt(View.GONE)
        Desk360Constants.getDeviceId()
    }

    fun getTicketList(showLoading: Boolean) {

        if (showLoading) {
            progress?.set(View.VISIBLE)
        }

        Desk360RetrofitFactory.instance.httpService.getTicket()
            .enqueue(object : BaseCallback<Desk360TicketListResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360TicketListResponse>,
                    response: Response<Desk360TicketListResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.data?.let { data ->
                            ticketSize?.value = data.size

                            val unreadList = data.filter { unread ->
                                unread.status == "unread"
                            } as ArrayList<Desk360TicketResponse>

                            RxBus.publish(hashMapOf("sizeTicketList" to data.size))
                            RxBus.publish(hashMapOf("unReadSizeTicketList" to unreadList.size))

                            ticketList?.value =
                                data.filter { it.status != "expired" } as ArrayList<Desk360TicketResponse>
                            Desk360CurrentTicketFragment.ticketSize =
                                ticketList?.value?.size ?: 0

                            expiredList?.value =
                                data.filter { it.status == "expired" } as ArrayList<Desk360TicketResponse>
                        } ?: kotlin.run {
                            ticketList?.value = null
                        }
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
        register {
            if (it) {
                getTicketList(showLoading)
            } else {
                progress?.set(View.GONE)
            }
        }
    }
}