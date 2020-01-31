package com.teknasyon.desk360.helper

import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import com.teknasyon.desk360.model.Desk360CacheConfig
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import com.teknasyon.desk360.viewmodel.GetTypesViewModel
import io.paperdb.Paper
import org.json.JSONObject
import java.util.*

/**
 * Created by seyfullah on 09/04/2019.
 */

object Desk360Constants {

    var currentTheme: Int = 1
    var deviceToken: String? = null
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

        if (app_key != "" && app_version != "" && baseURL != "" && device_token != "") {
            Paper.book().write(
                "desk360CacheConfig",
                Desk360CacheConfig(app_key, app_version, baseURL, deviceToken)
            )
        }

        val desk360CacheConfig = Paper.book().read<Desk360CacheConfig>("desk360CacheConfig")

        if (language_code == "")
            return false

        if (time_zone == "")
            return false

        desk360CacheConfig?.let {

            this.app_key = desk360CacheConfig.appKey
            this.app_version = desk360CacheConfig.appVersion
            this.baseURL = desk360CacheConfig.baseURL
            this.deviceToken = desk360CacheConfig.deviceToken

        } ?: run {

            this.app_key = app_key
            this.app_version = app_version
            this.baseURL = baseURL
            this.deviceToken = device_token
        }

        if (app_key == "")
            return false

        if (app_version == "")
            return false

        if (baseURL == "")
            return false

        if (device_token != null && device_token != "") {
            Desk360Config.instance.getDesk360Preferences()?.adId = device_token
        } else {
            Desk360Config.instance.getDesk360Preferences()?.adId = this.deviceToken
        }

        if (app_language == "") {
            this.language_code = Locale.getDefault().language
        } else {
            this.language_code = app_language
        }
        if (json_object != null) {
            this.jsonObject = json_object
        }
        this.time_zone = TimeZone.getDefault().id

        val desk360ConfigResponse = Desk360Config.instance.getDesk360Preferences()?.types
        currentType = desk360ConfigResponse

        if (currentType == null) {
            GetTypesViewModel()
        }

        return true
    }

    fun createDb(context: Context) {
        Paper.init(context)
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