package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360CreateScreenButtonIcon : ImageView {


    init {
        if (Desk360Constants.currentType?.data?.create_screen?.button_icon_is_hidden != true) {
            this.visibility= View.INVISIBLE
        } else {
            this.visibility= View.VISIBLE
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