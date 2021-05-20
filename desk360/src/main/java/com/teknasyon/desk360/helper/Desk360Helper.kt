package com.teknasyon.desk360.helper

import com.teknasyon.desk360.model.Desk360Register
import com.teknasyon.desk360.modelv2.Desk360ConfigRequestModel

object Desk360Helper {

    fun createDesk360ConfigRequestModel() = Desk360ConfigRequestModel().apply {
        language_code = Desk360Constants.manager?.languageCode.toString()
        language_code_tag = Desk360Constants.manager?.languageTag
        country_code = Desk360Constants.manager?.countryCode?.toUpperCase()
    }

    fun createDesk360RegisterRequestModel() = Desk360Register().apply {
        app_key = Desk360Constants.manager?.appKey
        device_id = Desk360Config.instance.getDesk360Preferences()?.adId
        app_platform = if (Desk360Constants.manager?.platform == Platform.HUAWEI) "Huawei" else "Android"
        app_version = Desk360Constants.manager?.appVersion
        language_code = Desk360Constants.manager?.languageCode
        time_zone = Desk360Constants.manager?.timeZone
    }
}