package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.helper.Desk360SDK

class Desk360CreateScreenUploadText : AppCompatTextView {

    init {
        Desk360SDK.config?.data?.let { data ->
            this.setTextColor(Color.parseColor(data.create_screen?.label_text_color))
            this.typeface = Typeface.createFromAsset(context?.assets, "Montserrat-Regular.ttf")
            this.visibility =
                if (data.create_screen?.added_file_is_hidden!!) View.VISIBLE else View.INVISIBLE
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