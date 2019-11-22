package com.teknasyon.desk360.helper

import android.content.Context
import android.util.AttributeSet
import android.widget.ProgressBar
import androidx.appcompat.view.ContextThemeWrapper
import com.teknasyon.desk360.R

class Desk360Loading(context: Context, attrs: AttributeSet) : ProgressBar(
    if (Desk360Constants.currentType?.data?.first_screen?.button_style_id  in listOf(1, 2, 3, 5)) {
        ContextThemeWrapper(context, R.style.LightAccent)
    } else {
        ContextThemeWrapper(context, R.style.DarkAccent)
    }, attrs
) {
    init {

        when (Desk360Constants.currentType?.data?.ticket_detail_screen?.button_style_id) {
            1, 2, 3, 5 -> {
                this.setBackgroundResource(R.drawable.light_theme_button_bg)
            }
            4 -> {
                this.setBackgroundResource(R.drawable.dark_theme_button_bg)
            }
            else -> {
                this.setBackgroundResource(R.drawable.light_theme_button_bg)

            }
        }
    }
}