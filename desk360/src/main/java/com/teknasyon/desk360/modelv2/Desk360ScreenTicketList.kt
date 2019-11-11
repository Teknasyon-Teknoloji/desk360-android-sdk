package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Desk360ScreenTicketList : Serializable {

    @SerializedName("tab_text_font_size")
    var tab_text_font_size: Int = 18

    @SerializedName("ticket_date_font_size")
    var ticket_date_font_size: Int = 12

    @SerializedName("tab_text_color")
    var tab_text_color: String = "#9298b1"

    @SerializedName("ticket_subject_font_size")
    var ticket_subject_font_size: Int = 16

    @SerializedName("tab_border_color")
    var tab_border_color: String = "#58b0fa"

    @SerializedName("tab_active_border_color")
    var tab_active_border_color: String = "#58b0fa"

    @SerializedName("tab_current_text")
    var tab_current_text: String = "Current"

    @SerializedName("tab_past_text")
    var tab_past_text: String = "Past"

    @SerializedName("tab_text_font_weight")
    var tab_text_font_weight: String = "regular"

    @SerializedName("ticket_subject_color")
    var ticket_subject_color: String = "#000000"

    @SerializedName("tab_text_active_color")
    var tab_text_active_color: String = "#58b0fa"

    @SerializedName("ticket_item_icon_color")
    var ticket_item_icon_color: String = "#58b0fa"

    @SerializedName("ticket_list_type")
    var ticket_list_type: Int = 1

    @SerializedName("title")
    var title: String = "Destek Taleplerim"

    @SerializedName("ticket_item_backgroud_color")
    var ticket_item_backgroud_color: String = "#ffffff"

    @SerializedName("ticket_list_backgroud_color")
    var ticket_list_backgroud_color: String = "#ffffff"

    @SerializedName("ticket_date_color")
    var ticket_date_color: String = "#b0b0b0"
}