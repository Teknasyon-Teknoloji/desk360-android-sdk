package com.teknasyon.desk360.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360SplashActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_APP_ID = "EXTRA_APP_ID"
        const val EXTRA_TARGET_ID = "EXTRA_TARGET_ID"
        const val EXTRA_TOKEN = "EXTRA_TOKEN"
        const val EXTRA_DEVICE_TOKEN = "EXTRA_DEVICE_TOKEN"
    }

    private var notificationToken: String? = null
    private var targetId: String? = null
    private var appId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_desk_360)

        intent.extras?.let { bundle ->
            appId = bundle.getString(EXTRA_APP_ID)
            targetId = bundle.getString(EXTRA_TARGET_ID)
            notificationToken = bundle.getString(EXTRA_TOKEN)
            val deviceId = bundle.getString(EXTRA_DEVICE_TOKEN)
            Desk360Config().context = this
            Desk360Constants.manager?.currentTheme = 1

            if (!deviceId.isNullOrEmpty())
                Desk360Config.instance.getDesk360Preferences()?.adId = deviceId

            Desk360Constants.desk360Config {
                val intent = Intent(this, Desk360BaseActivity::class.java)
                intent.putExtra(EXTRA_TARGET_ID, targetId)
                intent.putExtra(EXTRA_TOKEN, notificationToken)
                intent.putExtra(EXTRA_APP_ID, appId)
                startActivity(intent)
                finish()

            }
        }
    }
}