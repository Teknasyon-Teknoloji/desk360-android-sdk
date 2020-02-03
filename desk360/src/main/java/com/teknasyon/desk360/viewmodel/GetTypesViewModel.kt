package com.teknasyon.desk360.viewmodel

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.teknasyon.desk360.connection.BaseCallback
import com.teknasyon.desk360.connection.Desk360StylesRetrofitFactory
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import com.teknasyon.desk360.view.activity.Desk360BaseActivity
import com.teknasyon.desk360.view.activity.Desk360SplashActivity
import retrofit2.Call
import retrofit2.Response

class GetTypesViewModel(
    private val splashActivity: Desk360SplashActivity,
    private val notificationToken: String?,
    private val targetId: String?
) : ViewModel() {

    private fun getTypes() {

        val map = HashMap<String, String>()
        map["language_code"] = Desk360Constants.language_code.toString()

        Desk360StylesRetrofitFactory.instance.sslService.getTypes(map)
            .enqueue(object : BaseCallback<Desk360ConfigResponse>() {
                override fun onResponseSuccess(
                    call: Call<Desk360ConfigResponse>,
                    response: Response<Desk360ConfigResponse>
                ) {
                    if (response.isSuccessful) {
                        Desk360Config.instance.getDesk360Preferences()?.types = Desk360ConfigResponse()
                        Desk360Config.instance.getDesk360Preferences()?.types = response.body()

                        navigateToDesk360BaseActivity()

                    } else {
                        Log.d("Desk360DataV2", "Error")
                    }
                }

                override fun onFailure(call: Call<Desk360ConfigResponse>, t: Throwable) {
                    Log.d("asd", t.localizedMessage)
                    super.onFailure(call, t)
                }
            })
    }

    private fun navigateToDesk360BaseActivity() {

        val intent = Intent(splashActivity, Desk360BaseActivity::class.java)
        intent.putExtra("token", notificationToken)
        intent.putExtra("targetId", targetId)
        splashActivity.startActivity(intent)
        splashActivity.finish()
    }
}