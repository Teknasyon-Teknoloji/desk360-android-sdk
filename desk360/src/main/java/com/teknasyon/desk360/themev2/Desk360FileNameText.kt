package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360SDK


class Desk360FileNameText : AppCompatTextView {
    init {

        this.setTextColor(Color.parseColor(Desk360SDK.config?.data?.create_screen?.form_input_color))

        val face = Typeface.createFromAsset(context?.assets, "Montserrat-Regular.ttf")
        this.typeface = face

        if (Desk360SDK.config?.data?.create_screen?.added_file_is_hidden!!) {
            this.visibility = View.VISIBLE
        } else {
            this.visibility = View.INVISIBLE
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