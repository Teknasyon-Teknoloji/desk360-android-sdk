package com.teknasyon.desk360.helper

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import com.teknasyon.desk360.Desk360Application
import java.util.*


/**
 * Created by seyfullah on 09/04/2019.
 */

object Desk360Constants {
    var currentTheme: String = "light"
    var app_key: String? = ""

    fun desk360Config(current_theme: String, app_key: String, device_token: String? = null): Boolean {
        if (current_theme == "")
            return false
        if (app_key == "")
            return false
        if (device_token != null && device_token != "")
            Desk360Application.instance.getDesk360Preferences()?.adId = device_token
        this.currentTheme = current_theme
        this.app_key = app_key
        return true
    }

    fun countryCode(): String {
        val tm = Desk360Application.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (tm.networkCountryIso != null && tm.networkCountryIso != "") tm.networkCountryIso else Locale.getDefault().country
    }

    /**
     * Generated a unique [deviceId]
     *
     */
    fun getDeviceId() {
        val devicesId = Desk360Application.instance.getDesk360Preferences()?.adId
        if (devicesId != null && devicesId != "") {
            return
        }

        val date = Date()
        date.time

        val deviceId = date.time.toString() + Build.VERSION.SDK_INT + "-" + Build.VERSION.INCREMENTAL + Build.MODEL

        deviceId.let {
            Desk360Application.instance.getDesk360Preferences()!!.adId = it
        }
    }
}
