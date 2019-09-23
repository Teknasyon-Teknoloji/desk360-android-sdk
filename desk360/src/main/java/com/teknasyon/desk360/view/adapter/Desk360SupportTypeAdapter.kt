package com.teknasyon.desk360.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.teknasyon.desk360.R

class Desk360SupportTypeAdapter(
    context: Context, resourceId: Int,
    private val objects: List<String>
) : ArrayAdapter<String>(context, resourceId, objects) {

    override fun getDropDownView(
        position: Int, convertView: View?,
        parent: ViewGroup
    ): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    private fun getCustomView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.desk360_type_dropdown, parent, false)
        val label = row.findViewById<View>(R.id.dropdown) as TextView
        label.text = objects[position]
        return row
    }
}