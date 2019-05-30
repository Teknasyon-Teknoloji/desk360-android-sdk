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

class TicketListAdapter(context: Context?, private val ticketList: ArrayList<TicketResponse>) :
    RecyclerView.Adapter<TicketListAdapter.Holder>() {
    private var context: Context? = null
    private var binding: TicketListItemBinding? = null
    var clickItem: TicketOnClickListener? = null

    init {
        this.context = context
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        ticketList.let { list ->
            holder.binding.ticketName.text = list[position].name
            holder.binding.ticketDate.text = list[position].created
            holder.binding.ticketName.setTypeface(null, Typeface.NORMAL)
            when {
                list[position].status == "open" -> {
                }
                list[position].status == "read" -> {
                    binding?.messageStatus?.setImageResource(R.drawable.read_icon_theme_dark)
                }
                list[position].status == "unread" -> {
                    binding?.messageStatus?.setImageResource(R.drawable.unread_icon_theme_dark)
                    holder.binding.ticketName.setTypeface(null, Typeface.BOLD)
                }
                else -> {
                }
            }

            holder.itemView.setOnClickListener {
                clickItem?.selectTicket(list[position], position)
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

    class Holder(internal val binding: TicketListItemBinding) : RecyclerView.ViewHolder(binding.root.rootView)


    interface TicketOnClickListener {
        fun selectTicket(item: TicketResponse, position: Int)
    }
}