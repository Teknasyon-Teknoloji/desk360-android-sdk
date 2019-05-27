package com.teknasyon.desk360.connection

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseCallback<T> : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        onResponseSuccess(call, response)
    }

    abstract fun onResponseSuccess(call: Call<T>, response: Response<T>)

    override fun onFailure(call: Call<T>, t: Throwable) {
//        t.message?.let { Logger.log("", it) }
    }
}
