package com.teknasyon.desk360.helper

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import java.util.*

/**
 * Created by seyfullah on 09/04/2019.
 */

object Desk360Constants {

    var currentTheme: Int = 2
    var app_key: String? = null
    var app_version: String? = null
    var language_code: String? = null
    var time_zone: String? = null
    var baseURL: String? = null

    fun desk360Config(
        app_key: String, app_version: String, baseURL: String? = "http://teknasyon.desk360.com/", device_token: String? = null
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
        this.language_code = Locale.getDefault().language
        this.time_zone = TimeZone.getDefault().id
        this.baseURL = baseURL
        return true
    }

    fun desk360CurrentTheme(current_theme: Int) {
        if (current_theme == 1)
            return
        this.currentTheme = current_theme
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