package com.teknasyon.desk360.helper

import android.os.Build
import android.text.TextUtils
import com.teknasyon.desk360.model.Desk360Register
import com.teknasyon.desk360.modelv2.Desk360ConfigRequestModel
import java.util.Locale

object Desk360Helper {

    /**
     * Generates request model for Desk360 SDK Config Information
     * @return Desk360ConfigRequestModel
     * @see Desk360ConfigRequestModel
     */
    fun createDesk360ConfigRequestModel() = Desk360ConfigRequestModel().apply {
        language_code = Desk360SDK.manager?.languageCode.toString()
        language_code_tag = getLanguageTag()
        country_code = Desk360SDK.manager?.countryCode?.toUpperCase()
    }

    /**
     * Generates request model for Desk360 SDK registration
     * @return Desk360Register
     * @see Desk360Register
     */
    fun createDesk360RegisterRequestModel() = Desk360Register().apply {
        app_key = Desk360SDK.manager?.appKey
        device_id = Desk360Config.instance.getDesk360Preferences()?.adId
        app_platform =
            if (Desk360SDK.manager?.platform == Platform.HUAWEI) "Huawei" else "Android"
        app_version = Desk360SDK.manager?.appVersion
        language_code = Desk360SDK.manager?.languageCode
    }

    private fun getLanguageTag() =
        if (Desk360SDK.manager?.languageCode.isNullOrEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            Locale.getDefault().toLanguageTag().lowercase(Locale.getDefault())
        else
            null

    fun checkNotEmpty(value: String?, errorMessage: Any): String? {
        return if (TextUtils.isEmpty(value)) {
            throw IllegalArgumentException(errorMessage.toString())
        } else {
            value
        }
    }
}