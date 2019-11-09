package com.teknasyon.desk360.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360RetrofitFactory
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import retrofit2.Call
import retrofit2.Response

class GetTypesViewModel : ViewModel() {

    private var types: MutableLiveData<Desk360ConfigResponse> = MutableLiveData()

    fun getTypes(): MutableLiveData<Desk360ConfigResponse> {

        Desk360RetrofitFactory.instance.httpService.getTypes()
            .enqueue(object : BaseCallback<Desk360ConfigResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360ConfigResponse>,
                    response: Response<Desk360ConfigResponse>
                ) {
                    if (response.isSuccessful) {
                        types.value = response.body()
                        Desk360Constants.currentType=response.body()
                    }else{
                        Log.d("Desk360DataV2","Error")
                    }
                }
            })
        return types
    }
}