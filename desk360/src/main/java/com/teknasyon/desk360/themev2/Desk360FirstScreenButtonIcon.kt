package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView

import com.teknasyon.desk360.helper.Desk360Constants.currentType

class Desk360FirstScreenButtonIcon : ImageView {


    init {
        if (currentType?.data?.first_screen?.button_icon_is_hidden == true) {
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