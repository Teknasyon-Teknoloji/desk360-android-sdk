package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.helper.Desk360SDK

class Desk360CommonButtonText : AppCompatTextView {

    init {
        Desk360SDK.config?.data?.let { data ->
            this.setTextColor(Color.parseColor(data.first_screen?.button_text_color))
            data.first_screen?.button_text_font_size?.let { size ->
                this.textSize = size.toFloat()
            }
            this.text = data.first_screen?.button_text?.length?.let { length ->
                Desk360CustomStyle.setButtonText(
                    length,
                    data.first_screen.button_text
                )
            }
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )
}