package com.teknasyon.desk360examp

import android.content.Context
import com.teknasyon.desk360.helper.Desk360SDKManager
import com.teknasyon.desk360.helper.Platform
import org.json.JSONObject

object Desk360SDKHelper {
    fun setup(context: Context) {
        val desk360SDKManager = Desk360SDKManager.Builder(context)
            .setAppKey(BuildConfig.APP_KEY)
            .setAppVersion(BuildConfig.VERSION_NAME)
            .setLanguageCode("tr")
            .setPlatform(Platform.GOOGLE)
            .setCountryCode("de")
            .setCustomJsonObject(
                JSONObject(
                    "{\n" +
                            "  \"name\":\"Yasin\",\n" +
                            "  \"age\":30,\n" +
                            "  \"cars\": {\n" +
                            "    \"car1\":\"MERCEDES\",\n" +
                            "    \"car2\":\"BMW\",\n" +
                            "    \"car3\":\"AUDI\"\n" +
                            "  }\n" +
                            " }"
                )
            )
            .build()

        desk360SDKManager.initialize("", "deskt36012")
    }
}