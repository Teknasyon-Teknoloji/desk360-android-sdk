package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatEditText
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360EditTextMessage @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyle) {

    init {
        val color = when (Desk360Constants.currentTheme) {
            1 -> Color.BLACK
            4 -> Color.WHITE
            else -> Color.BLACK
        }

        setTextColor(color)
    }

}