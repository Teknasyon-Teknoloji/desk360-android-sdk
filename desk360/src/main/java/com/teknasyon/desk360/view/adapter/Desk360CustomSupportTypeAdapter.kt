package com.teknasyon.desk360.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.modelv2.Desk360Options

class Desk360CustomSupportTypeAdapter(
    context: Context, resourceId: Int,
    private val objects: List<Desk360Options>
) : ArrayAdapter<Desk360Options>(context, resourceId, objects) {
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
        val label = row.findViewById<View>(R.id.dropdown) as TextView
        label.text = objects[position].value
        return row
    }
}