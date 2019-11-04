package com.teknasyon.desk360.themev2

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants


class Desk360ConstraintLayout : ConstraintLayout {

    init {

        when (Desk360Constants.currentTheme) {
            1 -> {

                this.background = null
            }
            2 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow,null)
                }else{
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow)
                }
            }
            3 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow,null)
                }else{
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow)
                }
            }
            4 -> {
                this.background = null
            }
            5 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow,null)
                }else{
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow)
                }
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow,null)
                }else{
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow)
                }

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