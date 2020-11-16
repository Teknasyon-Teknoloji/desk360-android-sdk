package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360CreateScreenUploadText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val createScreen = Desk360Constants.currentType?.data?.create_screen

        this.setTextColor(Color.parseColor(createScreen?.label_text_color))
        this.typeface = Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")

        if (createScreen?.added_file_is_hidden == true) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.INVISIBLE
        }
    }
}