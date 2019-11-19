package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Desk360ScreenTicketDetail : Serializable{

     @SerializedName("chat_sender_date_color")
     var chat_sender_date_color: String = "#9298b1"

     @SerializedName("button_background_color")
     var button_background_color: String = "#58b0fa"

     @SerializedName("button_text_color")
     var button_text_color: String = "#ffffff"

     @SerializedName("button_border_color")
     var button_border_color: String = "#58b0fa"

     @SerializedName("button_text_font_weight")
     var button_text_font_weight: Int = 600

     @SerializedName("button_text_font_size")
     var button_text_font_size: Int = 18

     @SerializedName("button_icon_is_hidden")
     var button_icon_is_hidden: Boolean = false

//     @SerializedName("chat_sender_shadow_is_hidden")
//     var chat_sender_shadow_is_hidden: Boolean = false

//     @SerializedName("chat_receiver_shadow_is_hidden")
//     var chat_receiver_shadow_is_hidden: Boolean = false

//     @SerializedName("button_shadow_is_hidden")
//     var button_shadow_is_hidden: Boolean = false

     @SerializedName("chat_sender_font_weight")
     var chat_sender_font_weight: Int = 400

     @SerializedName("write_message_button_background_disable_color")
     var write_message_button_background_disable_color: String = "#58B0FA"

     @SerializedName("chat_receiver_date_color")
     var chat_receiver_date_color: String = "#9298b1"

     @SerializedName("write_message_button_icon_color")
     var write_message_button_icon_color: String = "#58b0fa"

     @SerializedName("write_message_button_icon_disable_color")
     var write_message_button_icon_disable_color: String = "#d0d0d0"

     @SerializedName("write_message_border_color")
     var write_message_border_color: String = "#c2c7cc"

     @SerializedName("button_text")
     var button_text: String = "Yeni Talep Ekle"

     @SerializedName("button_style_id")
     var button_style_id: Int = 1

     @SerializedName("write_message_place_holder_text")
     var write_message_place_holder_text: String = "Mesaj Yaz"

     @SerializedName("write_message_font_size")
     var write_message_font_size: Int = 16

     @SerializedName("write_message_place_holder_color")
     var write_message_place_holder_color: String = "#9298b1"

     @SerializedName("write_message_border_active_color")
     var write_message_border_active_color: String = "#000000"

     @SerializedName("chat_background_color")
     var chat_background_color: String = "#eeeff0"

     @SerializedName("write_message_font_weight")
     var write_message_font_weight: Int = 400

     @SerializedName("chat_receiver_font_weight")
     var chat_receiver_font_weight: Int = 400

     @SerializedName("title")
     var title: String = "Destek Taleplerim"

     @SerializedName("write_message_text_color")
     var write_message_text_color: String = "#9298b1"

     @SerializedName("chat_receiver_background_color")
     var chat_receiver_background_color: String = "#ffffff"

     @SerializedName("chat_box_style")
     var chat_box_style: Int = 1

     @SerializedName("write_message_background_color")
     var write_message_background_color: String = "#ffffff"

     @SerializedName("chat_sender_text_color")
     var chat_sender_text_color: String = "#ffffff"

     @SerializedName("chat_receiver_font_size")
     var chat_receiver_font_size: Int = 16

     @SerializedName("chat_sender_background_color")
     var chat_sender_background_color: String = "#66748b"

     @SerializedName("write_message_button_background_color")
     var write_message_button_background_color: String = "#58B0FA"

     @SerializedName("chat_receiver_text_color")
     var chat_receiver_text_color: String = "#525A7E"

     @SerializedName("chat_sender_font_size")
     var chat_sender_font_size: Int = 16
}