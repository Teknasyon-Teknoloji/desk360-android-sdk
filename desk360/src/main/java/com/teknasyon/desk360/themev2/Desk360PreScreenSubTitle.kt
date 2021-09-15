package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360SDK

class Desk360PreScreenSubTitle : AppCompatTextView {
    init {
        this.setTextColor(Color.parseColor(Desk360SDK.config?.data?.create_pre_screen?.sub_title_color))
        this.text = Desk360SDK.config?.data?.create_pre_screen?.sub_title
        this.textSize =
            Desk360SDK.config?.data?.create_pre_screen?.sub_title_font_size!!.toFloat()
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}