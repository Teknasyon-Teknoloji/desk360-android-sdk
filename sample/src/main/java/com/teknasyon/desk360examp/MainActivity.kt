package com.teknasyon.desk360examp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.view.activity.Desk360BaseActivity

class MainActivity : AppCompatActivity(), LifecycleOwner {
    var openContact: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openContact = findViewById(R.id.openContactUs)

        openContact?.setOnClickListener { setupNavigation() }
    }

    private fun setupNavigation() {
        Desk360Config().context = this
        Desk360Constants.desk360CurrentTheme("light")
        Desk360Constants.desk360Config(
            app_key = BuildConfig.APP_KEY,
            app_version = BuildConfig.VERSION_NAME,
            baseURL = BuildConfig.DESK360_BASE_URL
        )
        startActivity(Intent(this, Desk360BaseActivity::class.java))
    }
}