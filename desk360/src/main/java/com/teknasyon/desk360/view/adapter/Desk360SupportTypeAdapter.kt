package com.teknasyon.desk360.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.helper.Desk360Constants

class Desk360SupportTypeAdapter(
    context: Context, resourceId: Int,
    private val objects: List<String>
) : ArrayAdapter<String>(context, resourceId, objects) {
    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        return getCustomView(position, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, parent)
    }

    private fun getCustomView(position: Int, parent: ViewGroup): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.desk360_type_dropdown, parent, false)
        if (Desk360Constants.currentType?.data?.create_screen?.form_style_id == 3)
            row.setBackgroundColor(
                Color.parseColor(
                    Desk360Constants.currentType?.data?.create_screen?.form_input_background_color
                        ?: "#ffffff"
                )
            )
        else row.setBackgroundColor(
            Color.parseColor(
                Desk360Constants.currentType?.data?.general_settings?.main_background_color
                    ?: "#ffffff"
            )
        )
        val label = row.findViewById<View>(R.id.dropdown) as TextView

        if (position == 0) {
            label.text = Desk360Constants.currentType?.data?.general_settings?.subject_field_text
            label.setTextColor(
                Color.parseColor(
                    Desk360Constants.currentType?.data?.create_screen?.label_text_color
                        ?: "#000000"
                )
            )

        } else {
            label.text = objects[position]
            label.setTextColor(
                Color.parseColor(
                    Desk360Constants.currentType?.data?.create_screen?.form_input_focus_color
                        ?: "#000000"
                )
            )
        }

        return row
    }
}