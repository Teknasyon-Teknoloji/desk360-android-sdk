package com.teknasyon.desk360examp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.view.activity.Desk360BaseActivity

class MainActivity : AppCompatActivity(), LifecycleOwner {
    var openContact: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Desk360Constants.app_key = "123456"
        Desk360Constants.currentTheme = "light "
        openContact = findViewById(R.id.openContactUs)

        openContact?.setOnClickListener { setupNavigation() }

    }

    private fun setupNavigation() {
        startActivity(Intent(this, Desk360BaseActivity::class.java))
    }
}
