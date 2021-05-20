package com.teknasyon.desk360examp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360SDKManager
import com.teknasyon.desk360.helper.Environment
import com.teknasyon.desk360.helper.Platform
import org.json.JSONObject

class MainActivity : AppCompatActivity(), LifecycleOwner {
    var openContact: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openContact = findViewById(R.id.openContactUs)
        openContact?.setOnClickListener { setupNavigation() }
    }

    private fun setupNavigation() {
        Desk360Config.instance.context = this

        val desk360SDKManager = Desk360SDKManager.Builder()
            .appKey(BuildConfig.APP_KEY)
            .appVersion(BuildConfig.VERSION_NAME)
            .languageCode("tr")
            .environment(Environment.PRODUCTION)
            .platform(Platform.GOOGLE)
            .countryCode("de")
            .theme(1)
            .jsonObject(
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
            .addIntentFlags(
                arrayOf(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP,
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                )
            )
            .build()

        val desk360Client = desk360SDKManager.initialize("", "", "deskt36012")

        desk360Client.start(this)
    }
}