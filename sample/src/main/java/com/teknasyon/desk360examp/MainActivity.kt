package com.teknasyon.desk360examp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.teknasyon.desk360.helper.Desk360SDK

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Desk360SDKHelper.setup(this)

        findViewById<View>(R.id.openContactUs)?.setOnClickListener {
            Desk360SDK.start()
        }
    }
}