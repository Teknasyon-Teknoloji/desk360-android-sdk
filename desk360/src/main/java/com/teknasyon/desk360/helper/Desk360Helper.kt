package com.teknasyon.desk360.helper

import android.os.Build
import android.text.TextUtils
import com.teknasyon.desk360.model.Desk360Register
import com.teknasyon.desk360.modelv2.Desk360ConfigRequestModel
import java.util.*

object Desk360Helper {

    fun createDesk360ConfigRequestModel() = Desk360ConfigRequestModel().apply {
        language_code = Desk360Constants.manager?.languageCode.toString()
        language_code_tag = getLanguageTag()
        country_code = Desk360Constants.manager?.countryCode?.toUpperCase()
    }

    fun createDesk360RegisterRequestModel() = Desk360Register().apply {
        app_key = Desk360Constants.manager?.appKey
        device_id = Desk360Config.instance.getDesk360Preferences()?.adId
        app_platform =
            if (Desk360Constants.manager?.platform == Platform.HUAWEI) "Huawei" else "Android"
        app_version = Desk360Constants.manager?.appVersion
        language_code = Desk360Constants.manager?.languageCode
    }

    private fun getLanguageTag() =
        if (Desk360Constants.manager?.languageCode.isNullOrEmpty() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            Locale.getDefault().toLanguageTag().toLowerCase()
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