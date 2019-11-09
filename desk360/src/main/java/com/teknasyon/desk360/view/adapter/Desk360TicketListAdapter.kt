package com.teknasyon.desk360.view.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360TicketListItemBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.model.Desk360TicketResponse
import kotlinx.android.synthetic.main.desk360_ticket_list_item.view.*

class Desk360TicketListAdapter(
    context: Context?,
    private val ticketList: ArrayList<Desk360TicketResponse>
) :
    RecyclerView.Adapter<Desk360TicketListAdapter.Holder>() {
    private var context: Context? = null
    private var binding: Desk360TicketListItemBinding? = null
    var clickItem: TicketOnClickListener? = null

    init {
        this.context = context
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(holder.itemView) {
            ticket_subject.text = ticketList[position].message
            ticket_date.text = ticketList[position].created
            ticket_subject.setTypeface(null, Typeface.NORMAL)
            when {
                ticketList[position].status == "unread" -> {
                    if (Desk360Constants.currentTheme in listOf(1, 2, 3, 5)) {
                        message_status.setImageResource(R.drawable.mavi_zarf_icon)
                    } else {
                        message_status.setImageResource(R.drawable.zarf)
                    }
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
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.desk360_ticket_list_item,
                parent,
                false
            )
        return Holder(binding!!)
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    class Holder(internal val binding: Desk360TicketListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface TicketOnClickListener {
        fun selectTicket(item: Desk360TicketResponse, position: Int)
    }
}