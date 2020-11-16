package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360CommonFooterText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val generalSettings = Desk360Constants.currentType?.data?.general_settings
        this.setTextColor(Color.parseColor(generalSettings?.bottom_note_color))

        generalSettings?.bottom_note_font_size?.toFloat()?.let {
            this.textSize = it
        }

        val firstScreen = Desk360Constants.currentType?.data?.first_screen
        this.text = firstScreen?.bottom_note_text

        if (firstScreen?.bottom_note_is_hidden != true) {
            this.visibility = View.INVISIBLE
        } else {
            this.visibility = View.VISIBLE
        }
    }
}