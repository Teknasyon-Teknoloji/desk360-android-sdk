package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360SDK

class Desk360PreScreenFooter : AppCompatTextView {

    init {
        Desk360SDK.config?.data?.let { data ->
            this.setTextColor(Color.parseColor(data.general_settings?.bottom_note_color))
            this.textSize = data.general_settings?.bottom_note_font_size!!.toFloat()
            this.text = data.create_pre_screen?.bottom_note_text
            this.visibility =
                if (!data.create_pre_screen?.bottom_note_is_hidden!!) View.INVISIBLE else View.VISIBLE
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