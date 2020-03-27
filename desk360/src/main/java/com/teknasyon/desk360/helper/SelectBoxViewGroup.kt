package com.teknasyon.desk360.helper

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.teknasyon.desk360.modelv2.Desk360ScreenCreate


class SelectBoxViewGroup(val style: Desk360ScreenCreate, viewGroup: Fragment) {

    var holder: MyHolder
    var view: View? = null

    inner class MyHolder {
        var underline: View? = null
        var selectBox: Spinner? = null
        var shadowBorder: LinearLayout? = null
        var strokeView: ConstraintLayout? = null
        var selectBoxCardView: CardView? = null
    }

    init {
        view = viewGroup.layoutInflater.inflate(
            com.teknasyon.desk360.R.layout.custom_selectbox_layout,
            null
        )
        holder = MyHolder()
        holder.selectBoxCardView =
            view?.findViewById(com.teknasyon.desk360.R.id.select_box_card_view)
        holder.strokeView =
            view?.findViewById(com.teknasyon.desk360.R.id.stroke_view)
        holder.shadowBorder =
            view?.findViewById(com.teknasyon.desk360.R.id.shadow_border)
        holder.selectBox =
            view?.findViewById(com.teknasyon.desk360.R.id.select_box)
        holder.underline =
            view?.findViewById(com.teknasyon.desk360.R.id.underline)
        holder.underline?.setBackgroundColor(Color.parseColor(style.form_input_border_color))
    }

    fun createSpinner(): View? {

        val fourDp = Util.changeDp(holder.selectBox?.context, 4f)
        val sixDp = Util.changeDp(holder.selectBox?.context, 4f)
        val eightDp = Util.changeDp(holder.selectBox?.context, 8f)
        val tenDp = Util.changeDp(holder.selectBox?.context, 10f)
        val twelveDp = Util.changeDp(holder.selectBox?.context, 13f)

        holder.underline?.visibility = View.INVISIBLE
        holder.selectBoxCardView?.visibility = View.INVISIBLE

        holder.strokeView?.setStroke(style)

        when (style.form_style_id) {

            3 -> {

                holder.selectBox?.setPadding(fourDp, twelveDp, fourDp, twelveDp)

                holder.shadowBorder?.setStroke(style.form_input_border_color)
                holder.selectBoxCardView?.visibility = View.VISIBLE

                holder.strokeView?.setPadding(
                    0,
                    Util.changeDp(holder.strokeView?.context, 10f),
                    0,
                    Util.changeDp(holder.strokeView?.context, 10f)
                )
            }
            2 -> {

                holder.selectBox?.setPadding(fourDp, sixDp, fourDp, sixDp)

                holder.selectBoxCardView?.setBackgroundColor(
                    Color.parseColor(
                        Desk360Constants.currentType?.data?.general_settings?.main_background_color
                            ?: "#FFFFFF"
                    )
                )

                holder.strokeView?.setPadding(
                    0,
                    Util.changeDp(holder.strokeView?.context, 14f),
                    0,
                    Util.changeDp(holder.strokeView?.context, 8f)
                )
            }
            else -> {

                holder.selectBox?.setPadding(0, 0, 0, 0)
                setMargin(holder.selectBox)

                holder.strokeView?.setPadding(0,
                    Util.changeDp(holder.strokeView?.context, 9f),
                    0,
                    Util.changeDp(holder.strokeView?.context, 26f)
                )

                holder.selectBoxCardView?.setBackgroundColor(
                    Color.parseColor(
                        Desk360Constants.currentType?.data?.general_settings?.main_background_color
                            ?: "#FFFFFF"
                    )
                )
                holder.underline?.visibility = View.VISIBLE
                holder.underline?.setBackgroundColor(Color.parseColor(style.form_input_border_color))
            }
        }
        holder.selectBox?.setDesk360SpinnerStyle(style)
        return view
    }
}

fun Spinner.setDesk360SpinnerStyle(style: Desk360ScreenCreate) {
    background.setColorFilter(
        Color.parseColor(
            Desk360Constants.currentType?.data?.create_screen?.label_text_color
                ?: "#000000"
        ), PorterDuff.Mode.SRC_IN
    )
    if (style.form_style_id == 2) {
        val gd = GradientDrawable()
        gd.setColor(Color.TRANSPARENT)
        gd.cornerRadius = 24f
        gd.setStroke(3, Color.parseColor(style.form_input_border_color))
        this.background = gd
        //box
    }
}

fun ConstraintLayout.setStroke(style: Desk360ScreenCreate) {
    when (style.form_style_id) {
        1 -> {
            setPadding(3, 0, 3, 0)
            //line
        }
        2 -> {
            setPadding(6, 0, 0, 0)
            //box
        }
        else -> {
            //shadow
        }
    }
}

fun setMargin(selectBox: Spinner?) {

    val params = ConstraintLayout.LayoutParams(
        ConstraintLayout.LayoutParams.MATCH_PARENT,
        ConstraintLayout.LayoutParams.WRAP_CONTENT
    )
    params.setMargins(0, 8, 0, 8)
    selectBox?.layoutParams = params
}

fun LinearLayout.setStroke(borderColor: String) {
    //shadow
    val gd = GradientDrawable()
    gd.setColor(Color.TRANSPARENT)
    gd.cornerRadius = 16f
    gd.setStroke(1, Color.parseColor(borderColor))
    this.background = gd
}

