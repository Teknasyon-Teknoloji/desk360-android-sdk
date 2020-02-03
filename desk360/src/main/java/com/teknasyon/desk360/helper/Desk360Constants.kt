package com.teknasyon.desk360.helper

import android.app.Activity
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
        baseURL: String? = "http://teknasyon.desk360.com/",
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


        if (baseURL == "")
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
        this.baseURL = baseURL

        GetTypesViewModel()

        return true
    }

    fun startDesk360(
        activity: Activity,
        token: String,
        targetId: String,
        appKey: String,
        appVersion: String,
        appId: String,
        baseURL: String,
        deviceToken: String
    ) {

        val intent = Intent(activity, Desk360SplashActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("token", token)
        intent.putExtra("targetId", targetId)
        intent.putExtra("app_key", appKey)
        intent.putExtra("app_version", appVersion)
        intent.putExtra("baseURL", baseURL)
        intent.putExtra("device_token", deviceToken)
        intent.putExtra("appId", appId)

        activity.startActivity(intent)
        activity.finish()
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