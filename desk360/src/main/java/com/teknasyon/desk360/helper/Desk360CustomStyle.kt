package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView

object Desk360CustomStyle {

    fun setStyle(styleId: Int?, layout: ConstraintLayout, context: Context) {
        val params = layout.layoutParams as ConstraintLayout.LayoutParams
        when (styleId) {
            1, 2, 3 -> {
                params.setMargins(
                    convertDpToPixel(32f, context).toInt(),
                    0,
                    convertDpToPixel(32f, context).toInt(),
                    0
                )
            }
            4 -> {
                params.setMargins(0, 0, 0, 0)
            }
            else -> {
                params.setMargins(
                    convertDpToPixel(32f, context).toInt(),
                    0,
                    convertDpToPixel(32f, context).toInt(),
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
                    convertDpToPixel(16f, context).toInt(),
                    24,
                    convertDpToPixel(16f, context).toInt(),
                    0
                )
            }
            4 -> {
                params.setMargins(0, 24, 0, 0)
            }
            else -> {
                params.setMargins(
                    convertDpToPixel(16f, context).toInt(),
                    24,
                    convertDpToPixel(16f, context).toInt(),
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

        when (weight) {
            100 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Thin.ttf")
                textView.typeface = face
            }
            200 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-ExtraLight.ttf")
                textView.typeface = face
            }
            300 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Light.ttf")
                textView.typeface = face
            }
            400 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Regular.ttf")
                textView.typeface = face
            }
            500 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Medium.ttf")
                textView.typeface = face
            }
            600 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-SemiBold.ttf")
                textView.typeface = face
            }
            700 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Bold.ttf")
                textView.typeface = face
            }
            800 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-ExtraBold.ttf")
                textView.typeface = face
            }
            900 -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Black.ttf")
                textView.typeface = face
            }
            else -> {
                val face = Typeface.createFromAsset(context?.assets, "Montserrat-Regular.ttf")
                textView.typeface = face
            }
        }
    }

     fun setButtonText(buttonTextSize: Int,text:String?): String? {
        return if (buttonTextSize > 18) {
            text?.substring(0, 15) + "..."
        } else {
            text
        }
    }


    private fun convertDpToPixel(dpOfMargin: Float, context: Context): Float {
        return dpOfMargin * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }
}

