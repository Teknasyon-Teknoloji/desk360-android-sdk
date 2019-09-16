package com.teknasyon.desk360.helper

import android.content.Context
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Created by seyfullah on 14,June,2019
 *
 */

open class Desk360Config : KoinComponent {
    private val desk360ConfigWithKoin: Desk360ConfigWithKoin by inject()
    fun setContext(context: Context) {
        desk360ConfigWithKoin.desk360Context = context
    }
}

open class Desk360ConfigWithKoin {
    lateinit var desk360Context: Context
}