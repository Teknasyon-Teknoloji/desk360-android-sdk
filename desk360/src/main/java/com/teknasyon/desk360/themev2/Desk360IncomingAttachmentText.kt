package com.teknasyon.desk360.themev2

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360IncomingAttachmentText : TextView {

    init {

        this.setTextColor(Color.parseColor(Desk360Constants.currentType?.data?.ticket_detail_screen?.chat_sender_text_color))

        this.textSize = Desk360Constants.currentType?.data?.ticket_detail_screen?.chat_sender_font_size!!.toFloat()

        when (Desk360Constants.currentType?.data?.ticket_detail_screen?.chat_sender_font_weight) {
            100 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Thin.ttf")
                this.typeface = face
            }
            200 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-ExtraLight.ttf")
                this.typeface = face
            }
            300 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Light.ttf")
                this.typeface = face
            }
            400 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Regular.ttf")
                this.typeface = face
            }
            500 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Medium.ttf")
                this.typeface = face
            }
            600 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-SemiBold.ttf")
                this.typeface = face
            }
            700 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Bold.ttf")
                this.typeface = face
            }
            800 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-ExtraBold.ttf")
                this.typeface = face
            }
            900 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Black.ttf")
                this.typeface = face
            }
            else -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Regular.ttf")
                this.typeface = face
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