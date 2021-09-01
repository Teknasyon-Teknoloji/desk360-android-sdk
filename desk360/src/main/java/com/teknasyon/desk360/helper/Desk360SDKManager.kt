package com.teknasyon.desk360.helper

import androidx.annotation.NonNull
import com.teknasyon.desk360.helper.Desk360Helper.checkNotEmpty
import org.json.JSONObject
import java.util.*

class Desk360SDKManager internal constructor(builder: Builder) {

    fun initialize(_ticketId: String, _notificationToken: String?, _deviceId: String?): Desk360Client {
        Desk360Constants.manager = this

        return Desk360Client().apply {
            ticketId = _ticketId
            notificationToken = _notificationToken
            deviceId = _deviceId
        }
    }

    val appKey = builder.appKey
    val appVersion = builder.appVersion
    val languageCode = builder.languageCode
    val jsonObject = builder.jsonObject
    val platform = builder.platform
    val countryCode = builder.countryCode
    val name = builder.name
    val emailAddress = builder.emailAddress
    val enableHelpMode = builder.enableHelpMode

    class Builder {
        internal var appKey: String? = null
        internal var appVersion: String? = null
        internal var languageCode: String? = null
        internal var jsonObject: JSONObject? = null
        internal var platform: Platform = Platform.GOOGLE
        internal var countryCode: String? = null
        internal var name: String? = null
        internal var emailAddress: String? = null
        internal var enableHelpMode = true

        /**
         * Application Key
         */
        @NonNull
        fun setAppKey(key: String): Builder {
            require(key.isNotEmpty())

            if (key != Desk360Constants.manager?.appKey) {
                Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = false
            }

            return apply {
                this.appKey = checkNotEmpty(key, "Application key cannot be empty")
            }
        }

        /**
         * Application Version
         */
        @NonNull
        fun setAppVersion(appVersion: String) = apply {
            this.appVersion = checkNotEmpty(appVersion, "Application version cannot be empty")
        }

        /**
         * Language Code
         */
        fun setLanguageCode(languageCode: String) = apply {
            if (languageCode != Desk360Constants.manager?.languageCode) {
                Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = false
            }

            this.languageCode = languageCode.ifEmpty { Locale.getDefault().language }
        }

        /**
         * Platform
         * HUAWEI or GOOGLE
         * Default Value: GOOGLE
         */
        @NonNull
        fun setPlatform(platform: Platform): Builder {

            return apply {
                this.platform = platform
            }
        }

        /**
         * Country Code
         */
        fun setCountryCode(countryCode: String?) = apply {
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

        /** OPTIONAL
         * User Name
         */
        fun setUserName(name: String) = apply {
            this.name = name
        }

        /** OPTIONAL
         * User Email Address
         *
         */
        fun setUserEmailAddress(emailAddress: String) = apply {
            this.emailAddress = emailAddress
        }

        /** OPTIONAL
         * Help Mode
         */
        fun enableHelpMode(enableHelpMode: Boolean) = apply {
            this.enableHelpMode = enableHelpMode
        }

        /** OPTIONAL
         * Json Object
         */
        fun setCustomJsonObject(jsonObject: JSONObject?) = apply {
            this.jsonObject = jsonObject
        }

        @Deprecated(
            message = "we are going to replace with setAppKey",
            replaceWith = ReplaceWith(
                expression = "setAppKey(key)"
            )
        )
        fun appKey(key: String) = apply {
            if (key != Desk360Constants.manager?.appKey) {
                Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = false
            }

            this.appKey = key
        }

        @Deprecated(
            message = "we are going to replace with setAppVersion",
            replaceWith = ReplaceWith(
                expression = "setAppVersion(appVersion)"
            )
        )
        fun appVersion(appVersion: String) = apply {
            this.appVersion = appVersion
        }

        @Deprecated(
            message = "we are going to replace with setLanguageCode",
            replaceWith = ReplaceWith(
                expression = "setLanguageCode(languageCode)"
            )
        )
        fun languageCode(languageCode: String) = apply {
            if (languageCode != Desk360Constants.manager?.languageCode) {
                Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = false
            }

            if (languageCode == "") {
                this.languageCode = Locale.getDefault().language

            } else {
                this.languageCode = languageCode
            }
        }

        @Deprecated(
            message = "we are going to replace with setCustomJsonObject",
            replaceWith = ReplaceWith(
                expression = "setCustomJsonObject(jsonObject)"
            )
        )
        fun jsonObject(jsonObject: JSONObject?) = apply {
            this.jsonObject = jsonObject
        }

        @Deprecated(
            message = "we are going to replace with setPlatform",
            replaceWith = ReplaceWith(
                expression = "setPlatform(platform)"
            )
        )
        fun platform(platform: Platform) = apply {
            this.platform = platform
        }

        @Deprecated(
            message = "we are going to replace with setCountryCode",
            replaceWith = ReplaceWith(
                expression = "setCountryCode(countryCode)"
            )
        )
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

        @Deprecated(
            message = "we are going to replace with setUserName",
            replaceWith = ReplaceWith(
                expression = "setUserName(name)"
            )
        )
        fun name(name: String) = apply {
            this.name = name
        }

        @Deprecated(
            message = "we are going to replace with setUserEmailAddress",
            replaceWith = ReplaceWith(
                expression = "setUserEmailAddress(emailAddress)"
            )
        )
        fun emailAddress(emailAddress: String) = apply {
            this.emailAddress = emailAddress
        }

        fun build() = Desk360SDKManager(this)
    }
}