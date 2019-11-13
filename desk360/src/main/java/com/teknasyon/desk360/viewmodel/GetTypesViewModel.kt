package com.teknasyon.desk360.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360StylesRetrofitFactory
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360Preferences
import com.teknasyon.desk360.helper.PreferencesManager
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import retrofit2.Call
import retrofit2.Response

class GetTypesViewModel : ViewModel() {

    init {
        getTypes()
    }

    fun getTypes() {

        Desk360StylesRetrofitFactory.instance.sslService.getTypes()
            .enqueue(object : BaseCallback<Desk360ConfigResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360ConfigResponse>,
                    response: Response<Desk360ConfigResponse>
                ) {
                    if (response.isSuccessful) {

                        Desk360Config.instance.getDesk360Preferences()?.types=response.body()

                        if(Desk360Config.instance.getDesk360Preferences()?.types!=null){
                            Desk360Constants.currentType = Desk360Config.instance.getDesk360Preferences()?.types
                        }else{
                            Desk360Constants.currentType=response.body()
                        }


                    } else {
                        Log.d("Desk360DataV2", "Error")
                    }
                }

                override fun onFailure(call: Call<Desk360ConfigResponse>, t: Throwable) {
                    super.onFailure(call, t)
                    Log.d("asd", t.localizedMessage)
                }
            })
    }

}