package com.teknasyon.desk360.helper

import android.app.Activity
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> Activity.binding(
    @LayoutRes resId: Int
): Lazy<T> = lazy { DataBindingUtil.setContentView(this, resId) }