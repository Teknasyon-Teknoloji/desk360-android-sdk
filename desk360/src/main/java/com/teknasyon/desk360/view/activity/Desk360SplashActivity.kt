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

        val bundle = intent.extras
        bundle?.let {

            notificationToken = bundle.getString("token")
            targetId = bundle.getString("targetId")

            Desk360Config().context = this
            Desk360Constants.currentTheme = 1

            Desk360Constants.desk360Config(
                app_key = bundle.getString("app_key")!!,
                app_version = bundle.getString("app_version")!!,
                baseURL = bundle.getString("baseURL"),
                device_token = bundle.getString("device_token")
            )
        }

        val intent = Intent(this, Desk360BaseActivity::class.java)
        intent.putExtra(targetId, "targetId")
        intent.putExtra(notificationToken, "token")
        startActivity(intent)
        finish()
    }
}