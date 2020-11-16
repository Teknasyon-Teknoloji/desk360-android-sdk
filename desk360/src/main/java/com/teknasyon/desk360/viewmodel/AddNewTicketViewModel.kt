package com.teknasyon.desk360.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.teknasyon.desk360.connection.ApiException
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.model.*
import com.teknasyon.desk360.view.fragment.Desk360AddNewTicketFragment.Companion.SELECT_DOC
import com.teknasyon.desk360.view.fragment.Desk360AddNewTicketFragment.Companion.SELECT_VIDEO
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.File


/**
 * Created by seyfullah on 30,May,2019
 *
 */
open class AddNewTicketViewModel : ViewModel() {

    var typeList: MutableLiveData<ArrayList<Desk360Type>>? = MutableLiveData()
    var addedTicket: MutableLiveData<Desk360TicketResponse> = MutableLiveData()
    val error = MutableLiveData<Desk360Meta>()

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

        Desk360RetrofitFactory.instance.httpService.addTicket(ticketItem, filePart)
            .enqueue(object : BaseCallback<Desk360NewSupportResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360NewSupportResponse>,
                    response: Response<Desk360NewSupportResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        addedTicket.value =
                            response.body()?.data //todo işlem bittiğinde ticket ana sayfasına dönecek
                    } else {
                        addedTicket.value = null
                    }
                }

                override fun onFailure(call: Call<Desk360NewSupportResponse>, t: Throwable) {
                    super.onFailure(call, t)
                    when (t) {
                        is HttpException -> {
                            try {
                                val response = t.response()?.errorBody()?.string()
                                val result =
                                    Gson().fromJson<Desk360Meta>(response, Desk360Meta::class.java)

                                if (result != null)
                                    error.postValue(result)
                                else
                                    error.postValue(Desk360Meta(false, ErrorInfo("0", t.message)))
                            } catch (e: Throwable) {
                                error.postValue(Desk360Meta(false, ErrorInfo("0", t.message)))
                            }
                        }
                        is ApiException -> {
                            try {
                                val err = t.data
                                    .getJSONObject("meta")
                                    .getJSONObject("error")

                                error.postValue(
                                    Desk360Meta(
                                        false,
                                        ErrorInfo(
                                            err.getString("code"),
                                            err.getString("message")
                                        )
                                    )
                                )
                            } catch (e: Throwable) {
                                error.postValue(Desk360Meta(false, ErrorInfo("0", t.message)))
                            }
                        }
                        else -> {
                            error.postValue(Desk360Meta(false, ErrorInfo("0", t.message)))
                        }
                    }
                }
            })
    }

    private fun getFileType(resultLoadFiles: Int): String {
        return when (resultLoadFiles) {
            SELECT_VIDEO -> "video/*"
            SELECT_DOC -> "application/pdf"
            else -> "image/*"
        }
    }
}