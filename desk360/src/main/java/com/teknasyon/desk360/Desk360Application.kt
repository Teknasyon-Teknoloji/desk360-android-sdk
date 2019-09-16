package com.teknasyon.desk360

import android.app.Application
import com.teknasyon.desk360.helper.Desk360ConfigWithKoin
import com.teknasyon.desk360.helper.Desk360Preferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Created by seyfullah on 23,May,2019
 *
 */

class Desk360Application : Application() {
    private val myModule = module {
        single {
            Desk360Preferences(applicationContext)
        }
        single {
            Desk360ConfigWithKoin()
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            loadKoinModules(myModule)
            androidContext(this@Desk360Application)
        }
    }
}