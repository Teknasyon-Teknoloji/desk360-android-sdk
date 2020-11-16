package com.teknasyon.desk360.helper

import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.model.Desk360Register
import com.teknasyon.desk360.model.Desk360RegisterResponse
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import retrofit2.Call
import retrofit2.Response

fun getTypes(getTypeResponse: (status: Boolean) -> Unit = {}) {

    val map = HashMap<String, String?>()

    map["language_code"] = Desk360Constants.languageCode.toString()
    map["language_code_tag"] = Desk360Constants.languageTag

    Desk360RetrofitFactory.instance.httpService.getTypes(map)
        .enqueue(object : BaseCallback<Desk360ConfigResponse>() {

            override fun onResponseSuccess(
                call: Call<Desk360ConfigResponse>,
                response: Response<Desk360ConfigResponse>
            ) {

                if (response.isSuccessful) {

                    Desk360Config.instance.getDesk360Preferences()?.types = response.body()
                    Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = true

                    getTypeResponse(true)
                } else {
                    getTypeResponse(false)
                }
            }

            override fun onFailure(call: Call<Desk360ConfigResponse>, t: Throwable) {

                getTypeResponse(false)
                super.onFailure(call, t)
            }
        })
}

inline fun register(crossinline registerResponse: (status: Boolean) -> Unit = {}) {

    val register = Desk360Register(
        app_key = Desk360Constants.appKey,
        device_id = Desk360Config.instance.getDesk360Preferences()?.adId,
        app_platform = "Android",
        app_version = Desk360Constants.appVersion,
        language_code = Desk360Constants.languageCode,
        time_zone = Desk360Constants.timeZone
    )

    Desk360RetrofitFactory.instance.sslService.register(register)
        .enqueue(object : BaseCallback<Desk360RegisterResponse>() {

            override fun onResponseSuccess(
                call: Call<Desk360RegisterResponse>,
                response: Response<Desk360RegisterResponse>
            ) {

                if (response.isSuccessful && response.body() != null) {

                    Desk360Config.instance.getDesk360Preferences()?.data = response.body()!!.data
                    Desk360Config.instance.getDesk360Preferences()?.meta = response.body()!!.meta

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