package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360SentIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    init {
        setColorFilter(Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.chat_receiver_text_color))
    }
}