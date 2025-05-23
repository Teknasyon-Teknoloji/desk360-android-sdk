package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360SDK


class Desk360MainTitle : AppCompatTextView {

    init {
        this.setTextColor(Color.parseColor(Desk360SDK.config?.data?.general_settings?.header_text_color))
        this.textSize =
            Desk360SDK.config?.data?.general_settings?.header_text_font_size?.toFloat() ?: 14f
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

}