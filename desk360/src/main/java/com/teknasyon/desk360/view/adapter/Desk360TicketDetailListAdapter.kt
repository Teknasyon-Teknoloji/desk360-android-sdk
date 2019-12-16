package com.teknasyon.desk360.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360IncomingMessageItemLayoutBinding
import com.teknasyon.desk360.databinding.Desk360SendMessageItemLayoutBinding
import com.teknasyon.desk360.model.Desk360Message
import kotlinx.android.synthetic.main.desk360_incoming_message_item_layout.view.*
import kotlinx.android.synthetic.main.desk360_send_message_item_layout.view.*

class Desk360TicketDetailListAdapter(private val ticketList: ArrayList<Desk360Message>,private val url :String?) : RecyclerView.Adapter<Desk360TicketDetailListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == 0) {
            val binding = DataBindingUtil.inflate<Desk360IncomingMessageItemLayoutBinding>(LayoutInflater.from(parent.context), R.layout.desk360_incoming_message_item_layout, parent, false)
            return ViewHolder(binding, 0)
        } else {
            val binding = DataBindingUtil.inflate<Desk360SendMessageItemLayoutBinding>(LayoutInflater.from(parent.context), R.layout.desk360_send_message_item_layout, parent, false)
            return ViewHolder(binding, 1)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = ticketList[position]
        if (holder.itemViewType == 1) {
            holder.itemView.message_send.text = message.message
            holder.itemView.date_send.text = message.created
            if(url!=null ){

            }

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
    (if (viewType == 0) (binding as Desk360IncomingMessageItemLayoutBinding).root else (binding as Desk360SendMessageItemLayoutBinding).root)
}
