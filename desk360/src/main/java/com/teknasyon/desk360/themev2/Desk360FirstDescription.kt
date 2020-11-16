package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360FirstDescription @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val firstScreen = Desk360Constants.currentType?.data?.first_screen

        this.setTextColor(Color.parseColor(firstScreen?.sub_title_color))
        this.text = firstScreen?.sub_title
        firstScreen?.sub_title_font_size?.toFloat()?.let {
            this.textSize = it
        }
    }
}