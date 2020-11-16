package com.teknasyon.desk360.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360SplashActivity : AppCompatActivity() {

    private var notificationToken: String? = null
    private var targetId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_desk_360)

        callingActivity
        val bundle = intent.extras
        bundle?.let {

            targetId = bundle.getString("targetId")
            notificationToken = bundle.getString("token")

            Desk360Config().context = this
            Desk360Constants.currentTheme = 1

            Desk360Constants.desk360Config(
                appKey = bundle.getString("app_key")!!,
                appVersion = bundle.getString("app_version")!!,
                isTest = bundle.getBoolean("isTest"),
                deviceToken = bundle.getString("device_token"),
                appLanguage = bundle.getString("app_language")!!
            ) {
                val intent = Intent(this, Desk360Activity::class.java)
                    intent.putExtra("targetId", targetId)
                    intent.putExtra("token", notificationToken)
                    startActivity(intent)
                    finish()
                }
        }
    }
}