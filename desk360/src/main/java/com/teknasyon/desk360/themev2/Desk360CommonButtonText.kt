package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants.currentType
import com.teknasyon.desk360.helper.Desk360CustomStyle

class Desk360CommonButtonText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val firstScreen = currentType?.data?.first_screen

        this.setTextColor(Color.parseColor(firstScreen?.button_text_color))

        firstScreen?.button_text_font_size?.toFloat()?.let {
            this.textSize = it
        }

        firstScreen?.button_text?.let {
            this.text = Desk360CustomStyle.setButtonText(it.length, it)
        }
    }
}