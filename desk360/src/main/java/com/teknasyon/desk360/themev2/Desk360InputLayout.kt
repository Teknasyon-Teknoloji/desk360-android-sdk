package com.teknasyon.desk360.themev2

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360InputLayout : TextInputLayout {

    init {

        when (Desk360Constants.currentTheme) {
            1 -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_NONE)
                context.setTheme(R.style.input_type1)
                invalidate()
            }
            2 -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_OUTLINE)
                context.setTheme(R.style.input_type1)
                invalidate()
            }
            3 -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_OUTLINE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow,null)
                }
                else
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow)

                context.setTheme(R.style.input_type1)
                invalidate()
            }
            4 -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_NONE)
                context.setTheme(R.style.input_type1)
                invalidate()
            }
            5 -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_OUTLINE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow,null)
                }

                else
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow)

                context.setTheme(R.style.input_type1)
                invalidate()
            }
            else -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_NONE)
                context.setTheme(R.style.input_type1)
                invalidate()
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

