package com.teknasyon.desk360examp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360SDKManager
import com.teknasyon.desk360.helper.Platform
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.openContactUs)?.setOnClickListener { setupNavigation() }
    }

    private fun setupNavigation() {
        Desk360Config.instance.context = this

        val desk360SDKManager = Desk360SDKManager.Builder()
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

        val desk360Client = desk360SDKManager.initialize("", "", "deskt36012")

        desk360Client.start(this)
    }
}