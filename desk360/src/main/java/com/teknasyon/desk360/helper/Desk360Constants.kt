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
    var language_tag: String? = null
    var time_zone: String? = null
    var jsonObject: JSONObject? = null
    var platform: Platform = Platform.GOOGLE
    var baseURL = "https://teknasyon.desk360.com/"
    var currentType: Desk360ConfigResponse? = null
        get() {
            field = Desk360Config.instance.getDesk360Preferences()?.types
            return field
        }
    var environment = "sandbox"
    var country_code: String? = null
    var name: String? = null
    var emailAddress: String? = null

    fun desk360Config(
        app_key: String,
        app_version: String,
        environment: String,
        device_token: String? = null,
        json_object: JSONObject? = null,
        app_language: String = "",
        app_country_code: String? = "",
        name: String? = null,
        emailAddress: String? = null,
        desk360ConfigResponse: (status: Boolean) -> Unit = {}
    ): Boolean {

        if (app_key == "" || app_version == "" || language_code == "" || time_zone == "")
            return false

        if (device_token != null && device_token != "")
            Desk360Config.instance.getDesk360Preferences()?.adId = device_token

        this.app_key = app_key
        this.app_version = app_version
        this.environment = environment
        this.name = name
        this.emailAddress = emailAddress

        if (app_language == "") {
            this.language_code = Locale.getDefault().language

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.language_tag = Locale.getDefault().toLanguageTag().toLowerCase()
            } else {
                this.language_tag = null
            }

        } else {
            this.language_code = app_language
            this.language_tag = null
        }

        if (app_country_code.isNullOrEmpty()) {
            this.country_code = Locale.getDefault().country
            if (this.country_code.isNullOrEmpty()) {
                this.country_code = "xx"
            }
        } else {
            this.country_code = app_country_code
        }

        if (json_object != null) {
            this.jsonObject = json_object
        }
        this.time_zone = TimeZone.getDefault().id

        val call = GetTypesViewModel()

        Desk360Config.instance.getDesk360Preferences()?.data?.let {

            if (Util.isTokenExpired(it.expired_at)) {

                call.register { status ->

                    if (status) {
                        checkType(desk360ConfigResponse, call)
                    } // TODO REGISTER OLAMAZ ISE BAKILACAK
                }
            } else {
                checkType(desk360ConfigResponse, call)
            }

        } ?: run {

            call.register { status ->

                if (status) {
                    checkType(desk360ConfigResponse, call)
                } // TODO REGISTER OLAMAZ ISE BAKILACAK
            }
        }

        return true
    }

    private fun checkType(
        desk360ConfigResponse: (status: Boolean) -> Unit,
        call: GetTypesViewModel
    ) {

        val isTypeFetched = Desk360Config.instance.getDesk360Preferences()!!.isTypeFetched

        if (isTypeFetched) {
            desk360ConfigResponse(true)
        } else {
            call.getTypes { configurationsResponse ->
                desk360ConfigResponse(configurationsResponse)
            }
        }
    }

    fun initDesk360(
        context: Context,
        token: String,
        targetId: String,
        appVersion: String,
        deviceToken: String,
        appKey: String,
        appLanguage: String,
        platform: Platform = Platform.GOOGLE,
        environment: String,
        appCountryCode: String,
        name: String? = null,
        emailAddress: String? = null
    ): Intent {
        this.platform = platform

        val intent = Intent(context, Desk360SplashActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.putExtra("environment", environment)
        intent.putExtra("token", token)
        intent.putExtra("targetId", targetId)
        intent.putExtra("app_key", appKey)
        intent.putExtra("app_version", appVersion)
        intent.putExtra("app_language", appLanguage)
        intent.putExtra("device_token", deviceToken)
        intent.putExtra("appId", context.applicationInfo.processName)
        intent.putExtra("app_country_code", appCountryCode)
        intent.putExtra("name", name)
        intent.putExtra("email_address", emailAddress)

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