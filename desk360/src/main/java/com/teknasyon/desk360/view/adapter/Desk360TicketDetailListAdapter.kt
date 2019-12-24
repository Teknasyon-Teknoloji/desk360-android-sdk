package com.teknasyon.desk360.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360IncomingMessageItemLayoutBinding
import com.teknasyon.desk360.databinding.Desk360SendMessageItemLayoutBinding
import com.teknasyon.desk360.model.Desk360Message
import kotlinx.android.synthetic.main.desk360_incoming_message_item_layout.view.*
import kotlinx.android.synthetic.main.desk360_send_message_item_layout.view.*

class Desk360TicketDetailListAdapter(
    private val ticketList: ArrayList<Desk360Message>,
    private val url: String?

) : RecyclerView.Adapter<Desk360TicketDetailListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if (viewType == 0) {
            val binding = DataBindingUtil.inflate<Desk360IncomingMessageItemLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.desk360_incoming_message_item_layout,
                parent,
                false
            )
            return ViewHolder(binding, 0)
        } else {
            val binding = DataBindingUtil.inflate<Desk360SendMessageItemLayoutBinding>(
                LayoutInflater.from(parent.context),
                R.layout.desk360_send_message_item_layout,
                parent,
                false
            )
            return ViewHolder(binding, 1)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = ticketList[position]
        if (holder.itemViewType == 1) {
            holder.itemView.message_send.text = message.message
            holder.itemView.date_send.text = message.created
            holder.itemView.webView.visibility = View.GONE
            holder.itemView.imageUrl.visibility = View.GONE
            if (url != null && url != "" && position == 0) {
                if (url.substring(url.length - 3) == "pdf") {
                    holder.itemView.webView.visibility = View.VISIBLE
                    holder.itemView.webView.loadUrl("https://desk-360.s3.amazonaws.com/attachment/52/desk360test/QOFN3o0jaPtdPnVmNJJpPSZtErvTHR1mKMvkr7i3.pdf")
                    val webSettings: WebSettings = holder.itemView.webView.settings
                    webSettings.javaScriptEnabled = true
                    webSettings.builtInZoomControls = true
                    val myPdfUrl =
                        "https://desk-360.s3.amazonaws.com/attachment/52/desk360test/QOFN3o0jaPtdPnVmNJJpPSZtErvTHR1mKMvkr7i3.pdf"
                    val url = "https://docs.google.com/viewer?embedded=true&url=$myPdfUrl"
                    holder.itemView.webView.loadUrl(url)

                } else {
                    holder.itemView.imageUrl.visibility = View.VISIBLE
                    Picasso.get().load(url).into(holder.itemView.imageUrl)
                }


            } else {
                holder.itemView.imageUrl.visibility = View.GONE
                holder.itemView.webView.visibility = View.GONE
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
