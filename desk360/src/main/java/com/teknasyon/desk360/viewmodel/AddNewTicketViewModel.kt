package com.teknasyon.desk360.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.model.Desk360NewSupportResponse
import com.teknasyon.desk360.model.Desk360TicketResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

/**
 * Created by seyfullah on 30,May,2019
 *
 */
open class AddNewTicketViewModel : ViewModel() {
    var addedTicket: MutableLiveData<Desk360TicketResponse> = MutableLiveData()
    val error = MutableLiveData<String>()

    fun addSupportTicket(
        ticketItem: HashMap<String, RequestBody>,
        file: File?,
        resultLoadFiles: Int
    ) {

        var filePart: MultipartBody.Part? = null

        if (file != null) {
            filePart = MultipartBody.Part.createFormData(
                "attachment",
                file.name,
                file.asRequestBody(getFileType(resultLoadFiles).toMediaTypeOrNull())
            )
        }
        Desk360RetrofitFactory.instance.desk360Service.addTicket(
            ticketItem = ticketItem,
            attachment = filePart
        )
            .enqueue(object : BaseCallback<Desk360NewSupportResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360NewSupportResponse>,
                    response: Response<Desk360NewSupportResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        addedTicket.value =
                            response.body()!!.data!!
                    } else {
                        addedTicket.value = null
                    }
                }

                override fun onError(code: String, message: String) {
                    super.onError(code, message)
                    addedTicket.value = null
                    error.value = message
                }
            })
    }

    private fun getFileType(resultLoadFiles: Int): String {

        return if (resultLoadFiles == 1223) "video/*"
        else "image/*"
    }
}