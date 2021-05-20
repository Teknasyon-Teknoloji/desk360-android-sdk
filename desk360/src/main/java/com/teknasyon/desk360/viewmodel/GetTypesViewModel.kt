package com.teknasyon.desk360.viewmodel

import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Helper
import com.teknasyon.desk360.model.Desk360RegisterResponse
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import retrofit2.Call
import retrofit2.Response

class GetTypesViewModel : ViewModel() {

    fun getTypes(getTypeResponse: (status: Boolean) -> Unit = {}) {
        Desk360RetrofitFactory.instance.desk360Service.getTypes(
            requestModel = Desk360Helper.createDesk360ConfigRequestModel()
        )
            .enqueue(object : BaseCallback<Desk360ConfigResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360ConfigResponse>,
                    response: Response<Desk360ConfigResponse>
                ) {

                    if (response.isSuccessful) {

                        Desk360Config.instance.getDesk360Preferences()?.types = response.body()
                        Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = true
                    }

                    getTypeResponse(response.isSuccessful)
                }

                override fun onFailure(call: Call<Desk360ConfigResponse>, t: Throwable) {

                    getTypeResponse(false)
                    super.onFailure(call, t)
                }
            })
    }

    fun register(registerResponse: (status: Boolean) -> Unit = {}) {
        Desk360RetrofitFactory.instance.desk360Service.register(Desk360Helper.createDesk360RegisterRequestModel())
            .enqueue(object : BaseCallback<Desk360RegisterResponse>() {

                override fun onResponseSuccess(
                    call: Call<Desk360RegisterResponse>,
                    response: Response<Desk360RegisterResponse>
                ) {

                    if (response.isSuccessful && response.body() != null) {

                        Desk360Config.instance.getDesk360Preferences()?.data =
                            response.body()!!.data
                        Desk360Config.instance.getDesk360Preferences()?.meta =
                            response.body()!!.meta

                        registerResponse(true)
                    } else {
                        registerResponse(false)
                    }
                }

                override fun onFailure(call: Call<Desk360RegisterResponse>, t: Throwable) {

                    registerResponse(false)
                    super.onFailure(call, t)
                }
            })
    }
}