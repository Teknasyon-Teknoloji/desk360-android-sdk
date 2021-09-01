package com.teknasyon.desk360.view.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360TicketListItemBinding
import com.teknasyon.desk360.helper.Desk360Constants
import com.teknasyon.desk360.helper.Desk360CustomStyle
import com.teknasyon.desk360.model.Desk360TicketResponse

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
        holder.bind(ticketList[position])

        holder.itemView.setOnClickListener {
            clickItem?.selectTicket(ticketList[position], position)
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

    inner class Holder(private val binding: Desk360TicketListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(ticket: Desk360TicketResponse) {
            binding.ticketSubject.text = ticket.message
            binding.ticketDate.text = ticket.created
            binding.ticketSubject.setTypeface(null, Typeface.NORMAL)

            when (ticket.status) {
                "unread" -> {
                    binding.messageStatus.setBackgroundResource(R.drawable.zarf)
                    binding.messageStatus.background?.setColorFilter(
                        Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_item_icon_color),
                        PorterDuff.Mode.SRC_ATOP
                    )
                    binding.ticketSubject.setTypeface(null, Typeface.BOLD)
                }
                "read" -> {
                    binding.ticketSubject.setTypeface(null, Typeface.NORMAL)
                    binding.messageStatus.setBackgroundResource(R.drawable.message_icon_read)
                    binding.messageStatus.background?.setColorFilter(
                        Color.parseColor(Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_item_icon_color),
                        PorterDuff.Mode.SRC_ATOP
                    )
                }
                else -> {
                    binding.messageStatus.visibility = View.INVISIBLE
                }
            }

            Desk360CustomStyle.setStyleTicket(
                Desk360Constants.currentType?.data?.ticket_list_screen?.ticket_list_type,
                binding.ticketItemRootLayout,
                context!!
            )
        }
    }

    interface TicketOnClickListener {
        fun selectTicket(item: Desk360TicketResponse, position: Int)
    }
}