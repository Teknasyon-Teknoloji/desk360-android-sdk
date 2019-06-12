package com.teknasyon.desk360.view.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.TicketListItemBinding
import com.teknasyon.desk360.model.TicketResponse
import kotlinx.android.synthetic.main.ticket_list_item.view.*

class TicketListAdapter(context: Context?, private val ticketList: ArrayList<TicketResponse>) :
    RecyclerView.Adapter<TicketListAdapter.Holder>() {
    private var context: Context? = null
    private var binding: TicketListItemBinding? = null
    var clickItem: TicketOnClickListener? = null

    init {
        this.context = context
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.itemView) {
            ticket_subject.text = ticketList[position].subject
            ticket_date.text = ticketList[position].created
            ticket_subject.setTypeface(null, Typeface.NORMAL)
            when {
                ticketList[position].status == "read" -> {
                    message_status.setImageResource(R.drawable.read_icon_theme_dark)
                }
                ticketList[position].status == "unread" -> {
                    message_status.setImageResource(R.drawable.unread_icon_theme_dark)
                    ticket_subject.setTypeface(null, Typeface.BOLD)
                }
                else -> {
                    message_status.setImageResource(android.R.color.transparent)
                }
            }

            setOnClickListener {
                clickItem?.selectTicket(ticketList[position], position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Holder {
        binding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.ticket_list_item, parent, false)
        return Holder(binding!!)
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    class Holder(internal val binding: TicketListItemBinding) : RecyclerView.ViewHolder(binding.root)

    interface TicketOnClickListener {
        fun selectTicket(item: TicketResponse, position: Int)
    }
}