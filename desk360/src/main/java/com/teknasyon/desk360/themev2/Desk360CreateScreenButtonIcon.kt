package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.teknasyon.desk360.helper.Desk360SDK

class Desk360CreateScreenButtonIcon : AppCompatImageView {

    init {
        this.visibility = if (Desk360SDK.config?.data?.create_screen?.button_icon_is_hidden != true)
            View.INVISIBLE
        else
            View.VISIBLE
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}