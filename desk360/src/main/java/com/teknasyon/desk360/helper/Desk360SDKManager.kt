package com.teknasyon.desk360.helper

import android.os.Build
import org.json.JSONObject
import java.util.*

class Desk360SDKManager internal constructor(builder: Builder) {

    fun initialize(_targetId: String, _token: String?, _deviceToken: String?): Desk360Client {
        Desk360Constants.manager = this

        return Desk360Client().apply {
            targetId = _targetId
            token = _token
            deviceToken = _deviceToken
        }
    }

    var currentTheme = builder.currentTheme
    val appKey = builder.appKey
    val appVersion = builder.appVersion
    val languageCode = builder.languageCode
    val languageTag = builder.languageTag
    val timeZone = builder.timeZone
    val jsonObject = builder.jsonObject
    val platform = builder.platform
    val environment = builder.environment
    val countryCode = builder.countryCode
    val name = builder.name
    val emailAddress = builder.emailAddress
    val intentFlags = builder.intentFlags
    val enableHelpMode = builder.enableHelpMode

    class Builder {
        internal var currentTheme: Int = 1
        internal var appKey: String? = null
        internal var appVersion: String? = null
        internal var languageCode: String? = null
        internal var languageTag: String? = null
        internal var timeZone = TimeZone.getDefault().id
        internal var jsonObject: JSONObject? = null
        internal var platform: Platform = Platform.GOOGLE
        internal var environment = "sandbox"
        internal var countryCode: String? = null
        internal var name: String? = null
        internal var emailAddress: String? = null
        internal var intentFlags: Array<Int>? = null
        internal var enableHelpMode = true

        fun theme(theme: Int) = apply {
            this.currentTheme = theme
        }

        fun appKey(key: String) = apply {
            if (key != Desk360Constants.manager?.appKey) {
                Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = false
            }

            this.appKey = key
        }

        fun appVersion(appVersion: String) = apply {
            this.appVersion = appVersion
        }

        fun languageCode(languageCode: String) = apply {
            if (languageCode != Desk360Constants.manager?.languageCode) {
                Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = false
            }

            if (languageCode == "") {
                this.languageCode = Locale.getDefault().language

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.languageTag = Locale.getDefault().toLanguageTag().toLowerCase()
                } else {
                    this.languageTag = null
                }

            } else {
                this.languageCode = languageCode
                this.languageTag = null
            }
        }

        fun jsonObject(jsonObject: JSONObject?) = apply {
            this.jsonObject = jsonObject
        }

        fun platform(platform: Platform) = apply {
            this.platform = platform
        }

        fun environment(environment: String) = apply {
            this.environment = environment
        }

        fun countryCode(countryCode: String?) = apply {
            if (countryCode != Desk360Constants.manager?.countryCode) {
                Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = false
            }

            if (countryCode.isNullOrEmpty()) {
                this.countryCode = Locale.getDefault().country
                if (this.countryCode.isNullOrEmpty()) {
                    this.countryCode = "xx"
                }
            } else {
                this.countryCode = countryCode
            }
        }

        fun name(name: String) = apply {
            this.name = name
        }

        fun emailAddress(emailAddress: String) = apply {
            this.emailAddress = emailAddress
        }

        fun addIntentFlags(intentFlags: Array<Int>) = apply {
            this.intentFlags = intentFlags
        }

        fun enableHelpMode(enableHelpMode: Boolean) = apply {
            this.enableHelpMode = enableHelpMode
        }

        fun build() = Desk360SDKManager(this)
    }
}