package com.teknasyon.desk360.helper

import android.content.Context
import android.os.Build
import android.telephony.TelephonyManager
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

/**
 * Created by seyfullah on 09/04/2019.
 */

object Desk360Constants : KoinComponent {
    private val desk360Preferences: Desk360Preferences by inject()
    private val desk360ConfigWithKoin: Desk360ConfigWithKoin by inject()
    var currentTheme: String = "light"
    var app_key: String? = null
    var app_version: String? = null
    var language_code: String? = null
    var time_zone: String? = null
    var baseURL: String? = null

    /**
     *
     * @param app_key  This key is a unique key from
     * @see <a href="http://desk360.com">desk360.com</a>
     *
     * @param app_version  app's current version
     * @param baseURL default value is "http://teknasyon.desk360.com/"
     * @param device_token default value is generated random device_token
     * @return
     */

    fun desk360Initial(
        app_key: String, app_version: String, baseURL: String? = null, device_token: String? = null
    ): Boolean {

        if (app_key == "")
            return false

        if (app_version == "")
            return false

        if (baseURL == null)
            this.baseURL = "http://teknasyon.desk360.com/"

        if (device_token != null && device_token != "")
            desk360Preferences.adId = device_token

        this.app_key = app_key
        this.app_version = app_version
        this.language_code = Locale.getDefault().language
        this.time_zone = TimeZone.getDefault().id
        this.baseURL = baseURL
        return true
    }

    fun desk360CurrentTheme(current_theme: String) {
        if (current_theme == "")
            return
        this.currentTheme = current_theme
    }

    fun countryCode(): String {

        val tm =
            desk360ConfigWithKoin.desk360Context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (tm.networkCountryIso != null && tm.networkCountryIso != "") tm.networkCountryIso else Locale.getDefault().country
    }

    /**
     * Generated a unique [deviceId]
     *
     */
    fun getDeviceId() {
        val devicesId = desk360Preferences.adId
        if (devicesId != null && devicesId != "") {
            return
        }

        val date = Date()
        date.time

        val deviceId =
            date.time.toString() + Build.VERSION.SDK_INT + "-" + Build.VERSION.INCREMENTAL + Build.MODEL

        deviceId.let {
            desk360Preferences.adId = it
        }
    }
}