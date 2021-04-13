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
    private var appId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_desk_360)

        intent.extras?.let {bundle ->
            appId = bundle.getString("appId")
            targetId = bundle.getString("targetId")
            notificationToken = bundle.getString("token")

            Desk360Config().context = this
            Desk360Constants.currentTheme = 1

            Desk360Constants.desk360Config(
                app_key = bundle.getString("app_key")!!,
                app_version = bundle.getString("app_version")!!,
                environment = bundle.getString("environment")!!,
                device_token = bundle.getString("device_token"),
                app_language = bundle.getString("app_language")!!,
                app_country_code = bundle.getString("app_country_code"),
                name = bundle.getString("name"),
                emailAddress = bundle.getString("email_address")
            ) {

                val intent = Intent(this, Desk360BaseActivity::class.java)
                intent.putExtra("targetId", targetId)
                intent.putExtra("token", notificationToken)
                intent.putExtra("appId", appId)
                startActivity(intent)
                finish()

            }
        }
    }
}