package com.teknasyon.desk360examp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.teknasyon.desk360.helper.Desk360Constants

class MyService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val deskintent = Desk360Constants.initDesk360(
            context = this,
            token = "",
            targetId = "",
            appVersion = BuildConfig.VERSION_NAME,
            deviceToken = "deskt36012",
            appKey = BuildConfig.APP_KEY,
            appLanguage = "tr",
            isTest = true
        )

        val pendingIntent = PendingIntent.getActivity(
            this,
            1231,
            deskintent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        createNotificationChannel()

        val builder = NotificationCompat.Builder(this, "123").apply {
            setSmallIcon(R.drawable.close_icon)
            setContentTitle("adsasdasd")
            setContentText("asdsa")
            setContentIntent(pendingIntent)
            priority = NotificationCompat.PRIORITY_DEFAULT
        }

        with(NotificationManagerCompat.from(this)) {
            notify(123, builder.build())
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "asd"
            val descriptionText = "asd"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("123", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
