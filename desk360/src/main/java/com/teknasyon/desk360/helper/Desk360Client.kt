package com.teknasyon.desk360.helper

import android.content.Context
import android.content.Intent
import com.teknasyon.desk360.view.activity.Desk360SplashActivity

class Desk360Client {
    var ticketId: String? = ""
    var notificationToken: String? = ""
    var deviceId: String? = ""

    fun start(context: Context) {
        context.startActivity(getIntent(context))
    }

    fun getIntent(context: Context): Intent {
        return Intent(context, Desk360SplashActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            putExtra(Desk360SplashActivity.EXTRA_APP_ID, context.applicationInfo.processName)
            putExtra(Desk360SplashActivity.EXTRA_TARGET_ID, ticketId)
            putExtra(Desk360SplashActivity.EXTRA_TOKEN, notificationToken)
            putExtra(Desk360SplashActivity.EXTRA_DEVICE_TOKEN, deviceId)
        }
    }
}