package com.teknasyon.desk360.view.adapter

import android.content.Context
import android.net.Uri
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebSettings
import android.widget.MediaController
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teknasyon.desk360.R
import com.teknasyon.desk360.databinding.Desk360IncomingMessageItemLayoutBinding
import com.teknasyon.desk360.databinding.Desk360SendMessageItemLayoutBinding
import com.teknasyon.desk360.model.Desk360Message
import kotlinx.android.synthetic.main.desk360_incoming_message_item_layout.view.*
import kotlinx.android.synthetic.main.desk360_send_message_item_layout.view.*


class Desk360TicketDetailListAdapter(
    private val ticketList: ArrayList<Desk360Message>,
    private val url: String?, private val context: Context?

) : RecyclerView.Adapter<Desk360TicketDetailListAdapter.ViewHolder>() {

    companion object {
        const val INCOMING_MESSAGE_TYPE = 0
        const val OUTGOING_MESSAGE_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == INCOMING_MESSAGE_TYPE) {
            val binding = Desk360IncomingMessageItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ViewHolder(binding, INCOMING_MESSAGE_TYPE)
        } else {
            val binding = Desk360SendMessageItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ViewHolder(binding, OUTGOING_MESSAGE_TYPE)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ticket = ticketList[position]
        val messageText = ticket.message
        val isValidUrl = URLUtil.isValidUrl(URLUtil.guessUrl(messageText))

        if (holder.itemViewType == OUTGOING_MESSAGE_TYPE) {

            holder.itemView.message_send.text = messageText
            if (isValidUrl) {
                Linkify.addLinks(holder.itemView.message_send, Linkify.WEB_URLS)
                context?.let {
                    ContextCompat.getColor(
                        it, R.color.color_text_link
                    )
                }?.let { holder.itemView.message_send.setLinkTextColor(it) }
            }

            holder.itemView.date_send.text = ticket.created
            holder.itemView.webView.visibility = View.GONE
            holder.itemView.imageUrl.visibility = View.GONE
            holder.itemView.videoView.visibility = View.GONE
            holder.itemView.message_tick.setImageResource(
                if (ticket.tick == true)
                    R.drawable.cift
                else
                    R.drawable.tek
            )

            if (url != null && url != "" && position == 0) {

                when {
                    url.substring(url.length - 3) == "pdf" -> {

                        holder.itemView.webView.visibility = View.VISIBLE
                        holder.itemView.webView.loadUrl("https://desk-360.s3.amazonaws.com/attachment/52/desk360test/QOFN3o0jaPtdPnVmNJJpPSZtErvTHR1mKMvkr7i3.pdf")
                        val webSettings: WebSettings = holder.itemView.webView.settings
                        webSettings.javaScriptEnabled = true
                        webSettings.builtInZoomControls = true
                        val myPdfUrl =
                            "https://desk-360.s3.amazonaws.com/attachment/52/desk360test/QOFN3o0jaPtdPnVmNJJpPSZtErvTHR1mKMvkr7i3.pdf"
                        val url = "https://docs.google.com/viewer?embedded=true&url=$myPdfUrl"
                        holder.itemView.webView.loadUrl(url)

                    }
                    url.substring(url.length - 3) == "mp4" -> {

                        holder.itemView.videoView.visibility = View.VISIBLE
                        val video: Uri = Uri.parse(url)
                        val mediaController = MediaController(context)
                        mediaController.setAnchorView(holder.itemView.videoView)
                        holder.itemView.videoView.setMediaController(mediaController)
                        holder.itemView.videoView.setVideoURI(video)
                        holder.itemView.videoView.setOnPreparedListener { mp ->
                            mp.isLooping = true
                            holder.itemView.videoView.start()
                        }
                    }
                    else -> {

                        holder.itemView.imageUrl.visibility = View.VISIBLE
                        Glide.with(holder.itemView.imageUrl.context).load(url).into(holder.itemView.imageUrl)
                    }
                }


            } else {

                holder.itemView.videoView.visibility = View.GONE
                holder.itemView.imageUrl.visibility = View.GONE
                holder.itemView.webView.visibility = View.GONE
            }

        } else {

            holder.itemView.date_incoming.text = ticket.created
            holder.itemView.message_incoming.text = messageText

            if (isValidUrl) {
                Linkify.addLinks(holder.itemView.message_incoming, Linkify.WEB_URLS)
                context?.let {
                    ContextCompat.getColor(
                        it, R.color.color_text_link
                    )
                }?.let { holder.itemView.message_incoming.setLinkTextColor(it) }
            }
        }


    }

    override fun getItemViewType(position: Int) =
        if (ticketList[position].is_answer == true) INCOMING_MESSAGE_TYPE else OUTGOING_MESSAGE_TYPE

    override fun getItemCount() = ticketList.size

    class ViewHolder(binding: Any?, viewType: Int) : RecyclerView.ViewHolder
        (if (viewType == INCOMING_MESSAGE_TYPE) (binding as Desk360IncomingMessageItemLayoutBinding).root else (binding as Desk360SendMessageItemLayoutBinding).root)
}
