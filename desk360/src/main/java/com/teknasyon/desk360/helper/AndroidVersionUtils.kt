package com.teknasyon.desk360.helper

import android.os.Build

object AndroidVersionUtils {
    fun isAtLeastMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}