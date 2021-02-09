package com.teknasyon.desk360examp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Platform
import org.json.JSONException
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
        Desk360Constants.currentTheme = 1

        val intent = Desk360Constants.initDesk360(
            context = this,
            token = "",
            targetId = "",
            appVersion = BuildConfig.VERSION_NAME,
            deviceToken = "deskt36012",
            appKey = BuildConfig.APP_KEY,
            isTest = true,
            appLanguage = "tr",
            platform = Platform.GOOGLE,
            appCountryCode = "de"
        )

        startActivity(intent)
    }
}