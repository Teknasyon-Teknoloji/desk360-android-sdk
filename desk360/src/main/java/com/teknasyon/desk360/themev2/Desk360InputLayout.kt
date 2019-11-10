package com.teknasyon.desk360.themev2

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.view.ContextThemeWrapper
import com.google.android.material.textfield.TextInputLayout
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants.currentTheme

class Desk360InputLayout : TextInputLayout {

    init {

        when (currentTheme) {
            1 -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_NONE)
            }
            2 -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_OUTLINE)
            }
            3 -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_OUTLINE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow, null)
                } else {
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow)
                }
            }
            4 -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_NONE)
            }
            5 -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_OUTLINE)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow, null)
                } else {
                    this.background = resources.getDrawable(R.drawable.type_3_bg_with_shadow)
                }
            }
            else -> {
                this.setBoxBackgroundMode(BOX_BACKGROUND_NONE)
            }
        }
    }

    constructor(context: Context) : super(
        when (currentTheme) {
            1 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            2 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            3 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            4 -> {
                ContextThemeWrapper(context, R.style.input_type4)
            }
            5 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            else -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }

        }
    )

    constructor(context: Context, attrs: AttributeSet) : super(
        when (currentTheme) {
            1 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            2 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            3 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            4 -> {
                ContextThemeWrapper(context, R.style.input_type4)
            }
            5 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            else -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }

        }, attrs
    )

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        when (currentTheme) {
            1 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            2 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            3 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            4 -> {
                ContextThemeWrapper(context, R.style.input_type4)
            }
            5 -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }
            else -> {
                ContextThemeWrapper(context, R.style.input_type1)
            }

        }, attrs, defStyle
    )
}

