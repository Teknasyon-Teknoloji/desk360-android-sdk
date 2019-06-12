package com.teknasyon.desk360.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.LightThemeIncomingMessageItemBinding
import com.teknasyon.desk360.databinding.LightThemeSendMessageItemBinding
import com.teknasyon.desk360.model.Message
import kotlinx.android.synthetic.main.light_theme_incoming_message_item.view.*
import kotlinx.android.synthetic.main.light_theme_send_message_item.view.*

class TicketDetailListAdapter(private val ticketList: ArrayList<Message>) : RecyclerView.Adapter<TicketDetailListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == 0) {
            val binding = DataBindingUtil.inflate<LightThemeIncomingMessageItemBinding>(LayoutInflater.from(parent.context), R.layout.light_theme_incoming_message_item, parent, false)
            return ViewHolder(binding, 0)
        } else {
            val binding = DataBindingUtil.inflate<LightThemeSendMessageItemBinding>(LayoutInflater.from(parent.context), R.layout.light_theme_send_message_item, parent, false)
            return ViewHolder(binding, 1)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = ticketList[position]
        if (holder.itemViewType == 1) {
            holder.itemView.message_send.text = message.message
            holder.itemView.date_send.text = message.created
        } else {
            holder.itemView.message_incoming.text = message.message
            holder.itemView.date_incoming.text = message.created
        }


    }

    override fun getItemViewType(position: Int): Int {
        return if (ticketList[position].is_answer) 0 else 1
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    class ViewHolder(binding: Any?, viewType: Int) : RecyclerView.ViewHolder
    (if (viewType == 0) (binding as LightThemeIncomingMessageItemBinding).root else (binding as LightThemeSendMessageItemBinding).root)
}
