package com.teknasyon.desk360.connection

import android.util.Log
import com.teknasyon.desk360.helper.ResponseListener
import com.teknasyon.desk360.helper.register
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
                        JSONObject(JSONObject(jsonObject.getString("meta")).getString("error")).getString(
                            "code"
                        )

                    if (errorCode == "expired_at") {
                        cloneRequest = call.clone()

                        register {
                            if (it) {
                                cloneRequest?.enqueue(this)
                            }
                        }
                    } else {
                        onFailure(call, ApiException(jsonObject))
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
        listener?.connectionError("", 123)
    }

    private fun retry() {
        cloneRequest?.clone()?.enqueue(this)
    }
}

class ApiException(
    val data: JSONObject
) : Exception()
