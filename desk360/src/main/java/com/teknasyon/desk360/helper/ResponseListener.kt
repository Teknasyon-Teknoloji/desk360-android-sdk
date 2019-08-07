package com.teknasyon.desk360.helper

/**
 * Created by seyfullah on 21,June,2019
 *
 */

interface ResponseListener {
    fun connectionError(message: String, code: Int)
}