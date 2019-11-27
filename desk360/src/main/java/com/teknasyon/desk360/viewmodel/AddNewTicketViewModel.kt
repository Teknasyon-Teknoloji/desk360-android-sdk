package com.teknasyon.desk360.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.model.Desk360NewSupportResponse
import com.teknasyon.desk360.model.Desk360TicketReq
import com.teknasyon.desk360.model.Desk360Type
import com.teknasyon.desk360.model.Desk360TypeResponse
import retrofit2.Call
import retrofit2.Response

/**
 * Created by seyfullah on 30,May,2019
 *
 */

open class AddNewTicketViewModel : ViewModel() {
    var typeList: MutableLiveData<ArrayList<Desk360Type>>? = MutableLiveData()
    var addedTicket: MutableLiveData<String> = MutableLiveData()

    init {
        getTypeList()
    }

    private fun getTypeList() {
        Desk360RetrofitFactory.instance.httpService.getTypeList()
            .enqueue(object : BaseCallback<Desk360TypeResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360TypeResponse>,
                    response: Response<Desk360TypeResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        typeList?.value = response.body()!!.data
                    } else {
                        typeList?.value = null
                    }
                }
            })
    }

    fun addSupportTicket(ticketItem: Desk360TicketReq) {
        Desk360RetrofitFactory.instance.httpService.addTicket(ticketItem)
            .enqueue(object : BaseCallback<Desk360NewSupportResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360NewSupportResponse>,
                    response: Response<Desk360NewSupportResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        addedTicket.value =
                            response.body()!!.data?.name //todo işlem bittiğinde ticket ana sayfasına dönecek
                    } else {
                        addedTicket.value = null
                    }
                }
            })
    }
}