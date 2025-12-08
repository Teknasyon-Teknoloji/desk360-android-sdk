package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.teknasyon.desk360.helper.Desk360SDK

class Desk360MainBackground @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    // Default: #F7F7F7
    private val DEFAULT_BG_COLOR = 0xFFF7F7F7.toInt()

    init {
        if (isInEditMode) {
            setBackgroundColor(DEFAULT_BG_COLOR)
        } else {
            setupBackgroundColor()
        }
    }

    private fun setupBackgroundColor() {
        val remoteColorString: String? = Desk360SDK.config?.data?.general_settings?.main_background_color

        try {
            if (!remoteColorString.isNullOrEmpty()) {
                val color = Color.parseColor(remoteColorString)
                setBackgroundColor(color)
            } else {
                setBackgroundColor(DEFAULT_BG_COLOR)
            }
        } catch (e: Exception) {
            setBackgroundColor(DEFAULT_BG_COLOR)
        }
    }
}