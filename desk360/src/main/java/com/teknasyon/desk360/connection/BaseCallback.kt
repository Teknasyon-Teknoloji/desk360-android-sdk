package com.teknasyon.desk360.connection

import android.util.Log
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.ResponseListener
import com.teknasyon.desk360.model.Desk360Register
import com.teknasyon.desk360.model.Desk360RegisterResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


abstract class BaseCallback<T> : Callback<T> {

    private var cloneRequest: Call<T>? = null
    private val call: Call<T>? = null
    private val retryCount = 0

    var listener: ResponseListener? = null

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.code() == 400) {
            if (!response.isSuccessful) {

                try {
                    val jsonObject = JSONObject(response.errorBody()?.string())

                    val errorCode: String =
                        JSONObject(JSONObject(jsonObject.getString("meta")).getString("error")).getString("code")

                    if (errorCode == "expired_at") {
                        cloneRequest = call.clone()
                        val register = Desk360Register()
                        register.app_key = Desk360Constants.app_key
                        register.device_id = Desk360Config.instance.getDesk360Preferences()?.adId
                        register.app_platform = "Android"
                        register.app_version = Desk360Constants.app_version
                        register.language_code = Desk360Constants.language_code
                        register.time_zone = Desk360Constants.time_zone

                        Desk360RetrofitFactory.instance.sslService.register(register)
                            .enqueue(object : Callback<Desk360RegisterResponse> {

                                override fun onFailure(call: Call<Desk360RegisterResponse>, t: Throwable) {

                                }

                                override fun onResponse(callRegister: Call<Desk360RegisterResponse>, response: Response<Desk360RegisterResponse>) {

                                    if (response.isSuccessful && response.body() != null) {

                                        Desk360Config.instance.getDesk360Preferences()?.data = response.body()!!.data
                                        Desk360Config.instance.getDesk360Preferences()?.meta = response.body()!!.meta

                                        cloneRequest?.enqueue(this@BaseCallback)

                                    }
                                }

                            })
                    }

                    return

                } catch (e: JSONException) {
                    e.printStackTrace()

                }
            }

        } else {
            onResponseSuccess(call, response)

        }
    }

    abstract fun onResponseSuccess(call: Call<T>, response: Response<T>)

    override fun onFailure(call: Call<T>, t: Throwable) {

        t.message?.let { Log.e("desk360-Failure", it) }
        listener?.let {
            listener!!.connectionError("", 123)
        }
    }

    private fun retry() {
        cloneRequest?.clone()?.enqueue(this)
    }
}
