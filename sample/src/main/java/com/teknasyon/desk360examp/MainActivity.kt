package com.teknasyon.desk360examp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.teknasyon.desk360.helper.Desk360Config
import com.teknasyon.desk360.helper.Desk360Constants
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
            this,
            "",
            "",
            BuildConfig.VERSION_NAME,
            "deskt36012",
            BuildConfig.APP_KEY,
            "tr",
            true
        )

        startActivity(intent)
    }
}