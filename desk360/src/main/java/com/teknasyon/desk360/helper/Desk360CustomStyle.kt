package com.teknasyon.desk360.helper

import android.content.Context
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

object Desk360CustomStyle {

    fun setStyle(styleId: Int?, layout: ConstraintLayout, context: Context) {
        val params = layout.layoutParams as ConstraintLayout.LayoutParams
        when (styleId) {
            1, 2, 3 -> {
                params.setMargins(
                    context.convertDpToPixel(32f).toInt(),
                    0,
                    context.convertDpToPixel(32f).toInt(),
                    0
                )
            }
            4 -> {
                params.setMargins(0, 0, 0, 0)
            }
            else -> {
                params.setMargins(
                    context.convertDpToPixel(32f).toInt(),
                    0,
                    context.convertDpToPixel(32f).toInt(),
                    0
                )
            }
        }

        layout.layoutParams = params
    }


    fun setStyleTicket(styleIdTicket: Int?, layout: ConstraintLayout, context: Context) {
        val params = layout.layoutParams as RecyclerView.LayoutParams
        when (styleIdTicket) {
            1, 2, 3 -> {
                params.setMargins(
                    context.convertDpToPixel(16f).toInt(),
                    24,
                    context.convertDpToPixel(16f).toInt(),
                    0
                )
            }
            4 -> {
                params.setMargins(0, 24, 0, 0)
            }
            else -> {
                params.setMargins(
                    context.convertDpToPixel(16f).toInt(),
                    24,
                    context.convertDpToPixel(16f).toInt(),
                    0
                )
            }
        }

        layout.layoutParams = params
    }

    fun setFontWeight(
        textView: TextView,
        context: Context?,
        weight: Int?
    ) {
        context ?: return
        textView.typeface = weight.toTypeFace(context)
    }

     fun setButtonText(buttonTextSize: Int,text:String?): String? {
        return if (buttonTextSize > 18) {
            text?.substring(0, 15) + "..."
        } else {
            text
        }
    }
}

