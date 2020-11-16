package com.teknasyon.desk360.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import com.teknasyon.desk360.modelv2.Desk360ConfigResponse
import com.teknasyon.desk360.view.activity.Desk360SplashActivity
import org.json.JSONObject
import java.util.*

object Desk360Constants {
    var currentTheme: Int = 1
    var appKey: String? = null
    var appVersion: String? = null
    var languageCode: String? = null
    var languageTag: String? = null
    var timeZone: String? = null
    private var isTest: Boolean = false
    private var deviceToken: String? = null
    var jsonObject: JSONObject? = null
    var baseURL: String? = null
    var currentType: Desk360ConfigResponse? = null
        get() {
            field = Desk360Config.instance.getDesk360Preferences()?.types
            return field
        }


    internal fun desk360Config(
        appKey: String,
        appVersion: String,
        isTest: Boolean,
        deviceToken: String? = null,
        jsonObject: JSONObject? = null,
        appLanguage: String = "",
        desk360ConfigResponse: (status: Boolean) -> Unit = {}
    ): Boolean {

        if (appKey == "")
            return false

        if (appVersion == "")
            return false

        if (languageCode == "")
            return false

        if (timeZone == "")
            return false

        configure(appKey, appVersion, isTest, deviceToken, jsonObject, appLanguage)

        val preferences = Desk360Config.instance.getDesk360Preferences()?.data

        if (preferences != null) {
            if (Util.isTokenExpired(preferences.expired_at)) {
                register { status ->
                    if (status) {
                        checkType(desk360ConfigResponse)
                    }
                }
            } else {
                checkType(desk360ConfigResponse)
            }
        } else {
            register { status ->
                if (status) {
                    checkType(desk360ConfigResponse)
                }
            }
        }

        return true
    }

    fun configure(
        appKey: String,
        appVersion: String,
        isTest: Boolean,
        deviceToken: String? = null,
        jsonObject: JSONObject? = null,
        appLanguage: String = "",
        theme: Int = 1
    ) {
        this.appKey = appKey
        this.appVersion = appVersion
        this.jsonObject = jsonObject
        this.currentTheme = theme

        this.timeZone = TimeZone.getDefault().id

        this.isTest = isTest
        this.baseURL = if (isTest) {
            "http://52.59.142.138:10380/"
        } else {
            "https://teknasyon.desk360.com/"
        }

        this.deviceToken = deviceToken
        if (!deviceToken.isNullOrBlank())
            Desk360Config.instance.getDesk360Preferences()?.adId = deviceToken

        if (appLanguage == "") {
            this.languageCode = Locale.getDefault().language
            this.languageTag = toBcp47Language(Locale.getDefault())
        } else {
            this.languageCode = appLanguage
        }
    }

    fun openDesk360(
        activity: Activity,
        targetId: String? = null,
        notificationToken: String? = null
    ) {
        val deviceToken =
            this.deviceToken ?: throw Exception("call Desk360Constants::configure first")
        val appKey = this.appKey ?: throw Exception("call Desk360Constants::configure first")
        val intent = initDesk360(
            activity,
            notificationToken ?: "",
            targetId ?: "",
            appVersion ?: activity.packageManager.getPackageInfo(
                activity.packageName,
                0
            ).versionName,
            deviceToken,
            appKey,
            languageCode ?: "",
            isTest
        )
        activity.startActivityForResult(intent, 9000)
    }

    private fun checkType(
        desk360ConfigResponse: (status: Boolean) -> Unit
    ) {
        val isTypeFetched = Desk360Config.instance.getDesk360Preferences()?.isTypeFetched == true

        if (isTypeFetched) {
            desk360ConfigResponse(true)
        } else {
            getTypes { configurationsResponse ->
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
        isTest: Boolean
    ): Intent {

        val intent = Intent(context, Desk360SplashActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.putExtra("isTest", isTest)
        intent.putExtra("token", token)
        intent.putExtra("targetId", targetId)
        intent.putExtra("app_key", appKey)
        intent.putExtra("app_version", appVersion)
        intent.putExtra("app_language", appLanguage)
        intent.putExtra("device_token", deviceToken)

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
                            RxBus.publish("refreshTickets")
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

    /**
     * Modified from:
     * https://github.com/apache/cordova-plugin-globalization/blob/master/src/android/Globalization.java
     *
     * Returns a well-formed ITEF BCP 47 language tag representing this locale string
     * identifier for the client's current locale
     *
     * @return String: The BCP 47 language tag for the current locale
     */
    private fun toBcp47Language(loc: Locale): String? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return loc.toLanguageTag()
        }

        // we will use a dash as per BCP 47
        val SEP = '-'
        var language = loc.language
        var region = loc.country
        var variant = loc.variant

        // special case for Norwegian Nynorsk since "NY" cannot be a variant as per BCP 47
        // this goes before the string matching since "NY" wont pass the variant checks
        if (language == "no" && region == "NO" && variant == "NY") {
            language = "nn"
            region = "NO"
            variant = ""
        }
        if (language.isEmpty() || !language.matches("\\p{Alpha}{2,8}".toRegex())) {
            language = "und" // Follow the Locale#toLanguageTag() implementation
            // which says to return "und" for Undetermined
        } else if (language == "iw") {
            language = "he" // correct deprecated "Hebrew"
        } else if (language == "in") {
            language = "id" // correct deprecated "Indonesian"
        } else if (language == "ji") {
            language = "yi" // correct deprecated "Yiddish"
        }

        // ensure valid country code, if not well formed, it's omitted
        if (!region.matches("\\p{Alpha}{2}|\\p{Digit}{3}".toRegex())) {
            region = ""
        }

        // variant subtags that begin with a letter must be at least 5 characters long
        if (!variant.matches("\\p{Alnum}{5,8}|\\p{Digit}\\p{Alnum}{3}".toRegex())) {
            variant = ""
        }
        val bcp47Tag = StringBuilder(language)
        if (region.isNotEmpty()) {
            bcp47Tag.append(SEP).append(region)
        }
        if (variant.isNotEmpty()) {
            bcp47Tag.append(SEP).append(variant)
        }
        return bcp47Tag.toString()
    }
}