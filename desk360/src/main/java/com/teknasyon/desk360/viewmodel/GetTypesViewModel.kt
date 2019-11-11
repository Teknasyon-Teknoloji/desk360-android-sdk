package com.teknasyon.desk360.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import retrofit2.Call
import retrofit2.Response

class GetTypesViewModel : ViewModel() {

    init {
        getTypes()
    }
    fun getTypes() {

        Desk360RetrofitFactory.instance.httpService.getTypes()
            .enqueue(object : BaseCallback<Desk360ConfigResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360ConfigResponse>,
                    response: Response<Desk360ConfigResponse>
                ) {
                    if (response.isSuccessful) {
                        Desk360Constants.currentType=response.body()
                    }else{
                        Log.d("Desk360DataV2","Error")
                    }
                }

                override fun onFailure(call: Call<Desk360ConfigResponse>, t: Throwable) {
                    super.onFailure(call, t)
                    Log.d("asd",t.localizedMessage)
                }
            })
    }
}