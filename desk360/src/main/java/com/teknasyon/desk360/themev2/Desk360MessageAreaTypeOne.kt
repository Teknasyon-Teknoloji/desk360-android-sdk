package com.teknasyon.desk360.themev2

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360MessageAreaTypeOne : ConstraintLayout {

    init {

        when (Desk360Constants.currentTheme) {
            1, 4 -> {
                this.visibility =View.INVISIBLE
            }
            2, 3, 5 -> {
                this.visibility = View.VISIBLE
            }
            else -> {
                this.visibility =View.INVISIBLE
            }
        }
    }


    constructor(context: Context) : super(

        when (Desk360Constants.currentTheme) {
            2 ,3,5-> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            else -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }

        }
    )

    constructor(context: Context, attrs: AttributeSet) : super(
        when (Desk360Constants.currentTheme) {
            2 ,3,5-> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            else -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }

        }, attrs
    )

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        when (Desk360Constants.currentTheme) {
            2 ,3,5-> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            else -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }

        }, attrs, defStyle
    )
}