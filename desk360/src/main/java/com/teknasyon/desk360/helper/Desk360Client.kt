package com.teknasyon.desk360.helper

import android.content.Context
import android.content.Intent
import com.teknasyon.desk360.view.activity.Desk360SplashActivity

class Desk360Client {
    var token: String? = ""
    var targetId: String? = ""
    var deviceToken: String? = ""

    fun start(context: Context) {
        context.startActivity(getIntent(context))
    }

    fun getIntent(context: Context): Intent {
        val intent = Intent(context, Desk360SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        Desk360Constants.manager?.let { manager ->
            manager.intentFlags?.forEach { flag ->
                intent.addFlags(flag)
            }

            intent.putExtra(Desk360SplashActivity.EXTRA_APP_ID, context.applicationInfo.processName)
            intent.putExtra(Desk360SplashActivity.EXTRA_TARGET_ID, targetId)
            intent.putExtra(Desk360SplashActivity.EXTRA_TOKEN, token)
            intent.putExtra(Desk360SplashActivity.EXTRA_DEVICE_TOKEN, deviceToken)
        }

        return intent
    }
}