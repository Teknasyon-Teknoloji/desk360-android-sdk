package com.teknasyon.desk360

import android.app.Application
import com.teknasyon.desk360.helper.AresPreferences

/**
 * Created by seyfullah on 23,May,2019
 *
 */

class Desc360Application : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        aresPreferences = AresPreferences()
    }
    fun getAresPreferences(): AresPreferences? {
        return aresPreferences
    }

    companion object {
        var INSTANCE: Desc360Application? = null
        private var aresPreferences: AresPreferences? = null

        val instance: Desc360Application
            get() {
                if (INSTANCE == null) {
                    INSTANCE = Desc360Application()
                }
                return INSTANCE as Desc360Application
            }
    }
}