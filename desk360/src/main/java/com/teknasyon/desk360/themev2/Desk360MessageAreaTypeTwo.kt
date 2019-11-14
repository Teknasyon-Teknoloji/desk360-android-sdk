package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360MessageAreaTypeTwo : ConstraintLayout {

    init {

        when (Desk360Constants.currentTheme) {
            1, 4 -> {
                this.visibility = View.VISIBLE
            }
            2, 3, 5 -> {

                this.visibility = View.INVISIBLE
            }

            else -> {
                this.visibility = View.VISIBLE
            }
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