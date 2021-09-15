package com.teknasyon.desk360.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360SDK
import com.teknasyon.desk360.helper.Desk360SDKManager

class Desk360SplashActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_APP_ID = "EXTRA_APP_ID"
        const val EXTRA_TARGET_ID = "EXTRA_TARGET_ID"
        const val EXTRA_TOKEN = "EXTRA_TOKEN"
        const val EXTRA_DEVICE_TOKEN = "EXTRA_DEVICE_TOKEN"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_desk_360)

        intent.extras?.let { bundle ->
            Desk360SDK.desk360Config {
                val intent = Intent(this, Desk360BaseActivity::class.java).apply {
                    putExtra(EXTRA_TARGET_ID, bundle.getString(EXTRA_TARGET_ID))
                    putExtra(EXTRA_TOKEN, bundle.getString(EXTRA_TOKEN))
                    putExtra(EXTRA_APP_ID, bundle.getString(EXTRA_APP_ID))
                }

                startActivity(intent)
                finish()
            }
        }
    }
}