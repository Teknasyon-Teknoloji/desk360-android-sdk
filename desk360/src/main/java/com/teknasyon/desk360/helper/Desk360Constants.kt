package com.teknasyon.desk360.helper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import com.teknasyon.desk360.view.activity.Desk360SplashActivity
import com.teknasyon.desk360.viewmodel.GetTypesViewModel
import org.json.JSONObject
import java.util.*

/**
 * Created by seyfullah on 09/04/2019.
 */

object Desk360Constants {

    var currentTheme: Int = 1
    var app_key: String? = null
    var app_version: String? = null
    var language_code: String? = null
    var time_zone: String? = null
    var jsonObject: JSONObject? = null
    var baseURL: String? = null
    var currentType: Desk360ConfigResponse? = null
        get() {
            field = Desk360Config.instance.getDesk360Preferences()?.types
            return field
        }


    fun desk360Config(
        app_key: String,
        app_version: String,
        isTest: Boolean,
        device_token: String? = null,
        json_object: JSONObject? = null,
        app_language: String = ""
    ): Boolean {

        if (app_key == "")
            return false

        if (app_version == "")
            return false

        if (language_code == "")
            return false

        if (time_zone == "")
            return false

        if (device_token != null && device_token != "")
            Desk360Config.instance.getDesk360Preferences()?.adId = device_token
        this.app_key = app_key
        this.app_version = app_version
        if (app_language == "") {
            this.language_code = Locale.getDefault().language
        } else {
            this.language_code = app_language
        }
        if (json_object != null) {
            this.jsonObject = json_object
        }
        this.time_zone = TimeZone.getDefault().id

        baseURL = if (isTest) {
            "http://52.59.142.138:10380/"
        } else {
            "http://teknasyon.desk360.com/"
        }

        GetTypesViewModel()

        return true
    }

    fun initDesk360(
        context: Context,
        token: String,
        targetId: String,
        appVersion: String,
        deviceToken: String,
        appKey: String,
        isTest: Boolean
    ): Intent {

        val intent = Intent(context, Desk360SplashActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.putExtra("isTest", isTest)
        intent.putExtra("token", token)
        intent.putExtra("targetId", targetId)
        intent.putExtra("app_key", appKey)
        intent.putExtra("app_version", appVersion)
        intent.putExtra("device_token", deviceToken)
        intent.putExtra("appId", context.applicationInfo.processName)

        return intent
    }

    fun getTicketId(hermes: String?): String? {

        try {
            hermes?.let {

                val targetDetail = JSONObject(hermes).getJSONObject("target_detail")
                targetDetail?.let {

                    val targetCategory = targetDetail.getString("target_category")
                    targetCategory?.let {

                        if (targetCategory == "Desk360Deeplink") {
                            return targetDetail.getString("target_id")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    fun countryCode(): String {

        val tm =
            Desk360Config.instance.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (tm.networkCountryIso != null && tm.networkCountryIso != "") tm.networkCountryIso else Locale.getDefault().country
    }

    /**
     * Generated a unique [deviceId]
     *
     */
    fun getDeviceId() {
        val devicesId = Desk360Config.instance.getDesk360Preferences()?.adId
        if (devicesId != null && devicesId != "") {
            return
        }

        val date = Date()
        date.time

        val deviceId =
            date.time.toString() + Build.VERSION.SDK_INT + "-" + Build.VERSION.INCREMENTAL + Build.MODEL

        deviceId.let {
            Desk360Config.instance.getDesk360Preferences()!!.adId = it
        }
    }
}