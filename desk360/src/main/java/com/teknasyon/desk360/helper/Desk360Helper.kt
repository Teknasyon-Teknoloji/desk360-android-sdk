package com.teknasyon.desk360.helper

import com.teknasyon.desk360.model.Desk360Register
import com.teknasyon.desk360.modelv2.Desk360ConfigRequestModel

object Desk360Helper {

    fun createDesk360ConfigRequestModel() = Desk360ConfigRequestModel().apply {
        language_code = Desk360Constants.language_code.toString()
        language_code_tag = Desk360Constants.language_tag
        country_code = Desk360Constants.country_code
    }

    fun createDesk360RegisterRequestModel() = Desk360Register().apply {
        app_key = Desk360Constants.app_key
        device_id = Desk360Config.instance.getDesk360Preferences()?.adId
        app_platform = if (Desk360Constants.platform == Platform.HUAWEI) "Huawei" else "Android"
        app_version = Desk360Constants.app_version
        language_code = Desk360Constants.language_code
        time_zone = Desk360Constants.time_zone
    }
}