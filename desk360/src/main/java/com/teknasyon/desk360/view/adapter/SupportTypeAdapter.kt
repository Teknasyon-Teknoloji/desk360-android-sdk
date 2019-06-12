package com.teknasyon.desk360.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.model.Type

class SupportTypeAdapter(val context: Context, var listItemsTxt: ArrayList<Type>) : BaseAdapter() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view: View
        val vh: ItemRowHolder

        if (convertView == null) {
            view = mInflater.inflate(R.layout.type_dropdown, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        val params = view.layoutParams
        params.height = 80
        view.layoutParams = params

        vh.label.text = listItemsTxt[position].title
        return view
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return listItemsTxt.size
    }

    private class ItemRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.dropdown) as TextView
    }
}