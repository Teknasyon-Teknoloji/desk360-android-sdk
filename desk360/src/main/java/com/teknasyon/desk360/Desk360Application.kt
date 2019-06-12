package com.teknasyon.desk360

import android.app.Application
import com.teknasyon.desk360.helper.Desk360Preferences

/**
 * Created by seyfullah on 23,May,2019
 *
 */

class Desc360Application : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        desk360Preferences = Desk360Preferences()
    }
    fun getAresPreferences(): Desk360Preferences? {
        return desk360Preferences
    }

    companion object {
        var INSTANCE: Desc360Application? = null
        private var desk360Preferences: Desk360Preferences? = null

        val instance: Desc360Application
            get() {
                if (INSTANCE == null) {
                    INSTANCE = Desc360Application()
                }
                return INSTANCE as Desc360Application
            }
    }
}