package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360CreateScreenUpluoadText : TextView {


    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.create_screen?.label_text_color))

        val face = Typeface.createFromAsset(context?.assets, "Montserrat-Regular.ttf")
        this.typeface = face

        if (Desk360Constants.currentType?.data?.create_screen?.added_file_is_hidden!!) {
            this.visibility= View.VISIBLE
        } else {
            this.visibility= View.INVISIBLE
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