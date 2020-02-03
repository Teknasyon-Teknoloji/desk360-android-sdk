package com.teknasyon.desk360.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teknasyon.desk360.R
import com.teknasyon.desk360.viewmodel.GetTypesViewModel

class Desk360SplashActivity : AppCompatActivity() {

    var notificationToken: String? = null
    var targetId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_desk_360)

        val bundle = intent.extras
        bundle?.let {
            notificationToken = bundle.getString("token")
            targetId = bundle.getString("targetId")
        }

        GetTypesViewModel(this,notificationToken,targetId)
    }
}