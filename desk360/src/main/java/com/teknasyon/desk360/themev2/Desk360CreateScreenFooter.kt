package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360CreateScreenFooter @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val generalSettings = Desk360Constants.currentType?.data?.general_settings

        setTextColor(Color.parseColor(generalSettings?.bottom_note_color))
        generalSettings?.bottom_note_font_size?.toFloat()?.let {
            this.textSize = it
        }

        val createScreen = Desk360Constants.currentType?.data?.create_screen

        text = createScreen?.bottom_note_text

        if (createScreen?.bottom_note_is_hidden == true) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.INVISIBLE
        }
    }
}