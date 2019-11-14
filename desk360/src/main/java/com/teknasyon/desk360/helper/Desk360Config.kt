package com.teknasyon.desk360.helper

import android.content.Context
import com.teknasyon.desk360.viewmodel.GetTypesViewModel

/**
 * Created by seyfullah on 14,June,2019
 *
 */
open class Desk360Config {
    private lateinit var desk360Context: Context
    var context: Context
        get() {
            return desk360Context
        }
        set(value) {
            desk360Context = value
            INSTANCE = this
            desk360Preferences = Desk360Preferences()
        }

    fun getDesk360Preferences(): Desk360Preferences? {
        return desk360Preferences
    }


    companion object {
        var INSTANCE: Desk360Config? = null
        private var desk360Preferences: Desk360Preferences? = null

        val instance: Desk360Config
            get() {
                if (INSTANCE == null) {
                    INSTANCE = Desk360Config()
                }
                return INSTANCE as Desk360Config
            }
    }
}