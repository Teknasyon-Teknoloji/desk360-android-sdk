package com.teknasyon.desk360.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.model.Desk360NewSupportResponse
import com.teknasyon.desk360.model.Desk360Type
import com.teknasyon.desk360.model.Desk360TypeResponse
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File


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
                        Desk360Config.instance.getDesk360Preferences()?.subjects = response.body()
                        typeList?.value = response.body()!!.data
                        Desk360Config.instance.getDesk360Preferences()?.subjects
                    } else {
                        typeList?.value =  Desk360Config.instance.getDesk360Preferences()?.subjects?.data
                    }
                }
            })
    }

    fun addSupportTicket(ticketItem: HashMap<String, RequestBody>, file: File?) {

        var filePart: MultipartBody.Part? = null

        if (file != null) {
            filePart = MultipartBody.Part.createFormData(
                "attachment",
                file.name,
                RequestBody.create(
                    MediaType.parse("image/*"), file
                )
            )
        }

        Desk360RetrofitFactory.instance.httpService.addTicket(ticketItem, filePart)
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