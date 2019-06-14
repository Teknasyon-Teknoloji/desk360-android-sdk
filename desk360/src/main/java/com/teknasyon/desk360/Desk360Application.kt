//package com.teknasyon.desk360
//
//import android.app.Application
//import com.teknasyon.desk360.helper.Desk360Preferences
//
///**
// * Created by seyfullah on 23,May,2019
// *
// */
//
//class Desk360Application : Application() {
//    override fun onCreate() {
//        super.onCreate()
//        INSTANCE = this
//        desk360Preferences = Desk360Preferences()
//    }
//    fun getDesk360Preferences(): Desk360Preferences? {
//        return desk360Preferences
//    }
//
//    companion object {
//        var INSTANCE: Desk360Application? = null
//        private var desk360Preferences: Desk360Preferences? = null
//
//        val instance: Desk360Application
//            get() {
//                if (INSTANCE == null) {
//                    INSTANCE = Desk360Application()
//                }
//                return INSTANCE as Desk360Application
//            }
//    }
//}