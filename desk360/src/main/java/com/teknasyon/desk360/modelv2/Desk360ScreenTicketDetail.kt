package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Desk360ScreenTicketDetail : Serializable{

     @SerializedName("chat_sender_date_color")
     var chat_sender_date_color: String = "#9298b1"

     @SerializedName("button_background_color")
     var button_background_color: String = "#ffffff"

     @SerializedName("chat_sender_font_weight")
     var chat_sender_font_weight: String = "regular"

     @SerializedName("chat_receiver_date_color")
     var chat_receiver_date_color: String = "#9298b1"

     @SerializedName("write_message_border_color")
     var write_message_border_color: String = "#c2c7cc"

     @SerializedName("write_message_place_holder_text")
     var write_message_place_holder_text: String = "Mesaj Yaz"

     @SerializedName("button_icon_color")
     var button_icon_color: String = "#58b0fa"

     @SerializedName("chat_background_color")
     var chat_background_color: String = "#cecece"

     @SerializedName("chat_receiver_font_weight")
     var chat_receiver_font_weight: String = "regular"

     @SerializedName("title")
     var title: String = "Destek Taleplerim"

     @SerializedName("write_message_text_color")
     var write_message_text_color: String = "#000000"

     @SerializedName("chat_receiver_background_color")
     var chat_receiver_background_color: String = "#ffffff"

     @SerializedName("button_icon_disable_color")
     var button_icon_disable_color: String = "#9298b1"

     @SerializedName("chat_box_style")
     var chat_box_style: Int = 1

     @SerializedName("write_message_background_color")
     var write_message_background_color: String = "#ffffff"

     @SerializedName("chat_sender_text_color")
     var chat_sender_text_color: String = "#ffffff"

     @SerializedName("chat_receiver_font_size")
     var chat_receiver_font_size: Int = 15

     @SerializedName("chat_sender_background_color")
     var chat_sender_background_color: String = "#66748b"

     @SerializedName("write_message_button_color")
     var write_message_button_color: String = "#58b0fa"

     @SerializedName("chat_receiver_text_color")
     var chat_receiver_text_color: String = "#525a7e"

     @SerializedName("chat_sender_font_size")
     var chat_sender_font_size: Int = 16
}