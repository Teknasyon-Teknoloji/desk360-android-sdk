package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.toTypeFace

class Desk360SentMessageLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyle) {

    init {
        val style = Desk360Constants.currentType?.data?.ticket_detail_screen

        setBackgroundResource(
            when (style?.chat_box_style) {
                1 -> R.drawable.sent_message_layout_type1
                2 -> R.drawable.sent_message_layout_type2
                3 -> R.drawable.sent_message_layout_type3
                4 -> R.drawable.sent_message_layout_type4
                else -> R.drawable.sent_message_layout_type1
            }
        )

        if (style?.chat_receiver_shadow_is_hidden == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                elevation = 20f
            }
        }

        background.colorFilter = PorterDuffColorFilter(
            Color.parseColor(style?.chat_receiver_background_color),
            PorterDuff.Mode.SRC_ATOP
        )

        setTextColor(Color.parseColor(style?.chat_receiver_text_color))
        setLinkTextColor(Color.parseColor(style?.chat_receiver_text_color))
        style?.chat_receiver_font_size?.toFloat()?.let { textSize = it }

        typeface = style?.chat_receiver_font_weight.toTypeFace(context)
    }
}