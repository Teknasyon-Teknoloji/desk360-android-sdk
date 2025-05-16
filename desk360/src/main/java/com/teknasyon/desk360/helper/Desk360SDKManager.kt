package com.teknasyon.desk360.helper

import android.content.Context
import com.teknasyon.desk360.helper.Desk360Helper.checkNotEmpty
import org.json.JSONObject
import java.util.Locale

class Desk360SDKManager internal constructor(builder: Builder) {

    /**
     * Desk360 SDK is initialized.
     * @param notificationToken the token of Notification for firebase
     * @param deviceId the id of Device
     */
    fun initialize(notificationToken: String?, deviceId: String?) {
        Desk360SDK.manager = this

        if (!deviceId.isNullOrEmpty())
            Desk360Config.instance.getDesk360Preferences()?.adId = deviceId

        Desk360SDK.client = Desk360Client().apply {
            this.notificationToken = notificationToken
            this.deviceId = deviceId
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

    class Builder(context: Context) {
        internal var appKey: String? = null
        internal var appVersion: String? = null
        internal var languageCode: String? = null
        internal var jsonObject: JSONObject? = null
        internal var platform: Platform = Platform.GOOGLE
        internal var countryCode: String? = null
        internal var name: String? = null
        internal var emailAddress: String? = null
        internal var enableHelpMode = true

        init {
            Desk360Config().context = context
        }

        /**
         * Sets the Application Key.
         * @param key the key of application
         * @return Desk360SDKManager.Builder
         * @see Desk360SDKManager.Builder
         */
        fun setAppKey(key: String): Builder {
            require(key.isNotEmpty())

            if (key != Desk360SDK.manager?.appKey) {
                Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = false
            }

            return apply {
                this.appKey = checkNotEmpty(key, "Application key cannot be empty")
            }
        }

        /**
         * Sets the Application Version.
         * @param version the version of application
         * @return Desk360SDKManager.Builder
         * @see Desk360SDKManager.Builder
         */
        fun setAppVersion(version: String) = apply {
            this.appVersion = checkNotEmpty(version, "Application version cannot be empty")
        }

        /**
         * Sets the Language Code.
         * @param code the code of Language
         * @return Desk360SDKManager.Builder
         * @see Desk360SDKManager.Builder
         */
        fun setLanguageCode(code: String) = apply {
            if (code != Desk360SDK.manager?.languageCode) {
                Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = false
            }

            this.languageCode = code.ifEmpty { Locale.getDefault().language }
        }

        /**
         * Sets the Mobile Service Platform.
         * HUAWEI or GOOGLE
         * Default Value: GOOGLE
         * @param platform
         * @return Desk360SDKManager.Builder
         * @see Desk360SDKManager.Builder
         */
        fun setPlatform(platform: Platform): Builder {

            return apply {
                this.platform = platform
            }
        }

        /**
         * Sets the Country Code.
         * @param code
         * @return Desk360SDKManager.Builder
         * @see Desk360SDKManager.Builder
         */
        fun setCountryCode(code: String?) = apply {
            if (code != Desk360SDK.manager?.countryCode) {
                Desk360Config.instance.getDesk360Preferences()?.isTypeFetched = false
            }

            if (code.isNullOrEmpty()) {
                this.countryCode = Locale.getDefault().country
                if (this.countryCode.isNullOrEmpty()) {
                    this.countryCode = "xx"
                }
            } else {
                this.countryCode = code
            }
        }

        /**
         * Sets the User Name.
         * @param name
         * @return Desk360SDKManager.Builder
         * @see Desk360SDKManager.Builder
         */
        fun setUserName(name: String) = apply {
            this.name = name
        }

        /**
         * Sets the Email Address.
         * @param emailAddress
         * @return Desk360SDKManager.Builder
         * @see Desk360SDKManager.Builder
         */
        fun setUserEmailAddress(emailAddress: String) = apply {
            this.emailAddress = emailAddress
        }

        /**
         * Sets the Help Mode.
         * @param enable
         * @return Desk360SDKManager.Builder
         * @see Desk360SDKManager.Builder
         */
        fun enableHelpMode(enable: Boolean) = apply {
            this.enableHelpMode = enable
        }

        /**
         * Sets the Json Object.
         * @param jsonObject
         * @return Desk360SDKManager.Builder
         * @see Desk360SDKManager.Builder
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
            if (key != Desk360SDK.manager?.appKey) {
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
            if (languageCode != Desk360SDK.manager?.languageCode) {
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
            if (countryCode != Desk360SDK.manager?.countryCode) {
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