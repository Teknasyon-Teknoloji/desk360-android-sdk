package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Desk360ScreenTicketList : Serializable {

    @SerializedName("tab_text_font_size")
    var tab_text_font_size: Int = 18

    @SerializedName("ticket_date_font_size")
    var ticket_date_font_size: Int = 12

    @SerializedName("tab_text_color")
    var tab_text_color: String = "#1F49B1"

    @SerializedName("ticket_subject_font_size")
    var ticket_subject_font_size: Int = 16

    @SerializedName("tab_border_color")
    var tab_border_color: String = "#39F721"

    @SerializedName("tab_active_border_color")
    var tab_active_border_color: String = "#FA0000"

    @SerializedName("tab_current_text")
    var tab_current_text: String = "Current"

    @SerializedName("tab_past_text")
    var tab_past_text: String = "Past"

    @SerializedName("tab_text_font_weight")
    var tab_text_font_weight: Int = 600

    @SerializedName("ticket_subject_color")
    var ticket_subject_color: String = "#2d2d2d"

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
    var ticket_list_backgroud_color: String = "#eeeff0"

    @SerializedName("ticket_date_color")
    var ticket_date_color: String = "#b0b0b0"

    @SerializedName("empty_icon_size")
    var empty_icon_size: Int = 64

    @SerializedName("empty_icon_color")
    var empty_icon_color: String = "#9298B1"


    @SerializedName("empty_button_text")
    var empty_button_text: String = "Yeni Talep Ekle"

    @SerializedName("empty_past_sub_title")
    var empty_past_sub_title: String = "Güncel Ticketınız Bulunmamaktadır"

    @SerializedName("empty_past_description")
    var empty_past_description: String =
        "İlk yardım mesajını yaratmak ve canlı destek almak için bize ulaşın"

    @SerializedName("empty_button_text_color")
    var empty_button_text_color: String = "#FFFFFF"

    @SerializedName("empty_current_sub_title")
    var empty_current_sub_title: String = "Güncel Ticketınız Bulunmamaktadır"

    @SerializedName("empty_button_border_color")
    var empty_button_border_color: String = "#58B0FA"

    @SerializedName("empty_current_description")
    var empty_current_description: String =
        "İlk yardım mesajını yaratmak ve canlı destek almak için bize ulaşın"

    @SerializedName("empty_past_sub_title_color")
    var empty_past_sub_title_color: String = "#4D4D4D"

    @SerializedName("empty_past_description_color")
    var empty_past_description_color: String = "#868686"

    @SerializedName("empty_button_background_color")
    var empty_button_background_color: String = "#58B0FA"

    @SerializedName("empty_current_sub_title_color")
    var empty_current_sub_title_color: String = "#4D4D4D"

    @SerializedName("empty_current_description_color")
    var empty_current_description_color: String = "#868686"

    @SerializedName("empty_button_style_id")
    var empty_button_style_id: Int = 1

    @SerializedName("empty_button_icon_is_hidden")
    var empty_button_icon_is_hidden: Boolean = false

    @SerializedName("empty_button_text_font_size")
    var empty_button_text_font_size: Int = 18

    @SerializedName("empty_button_text_font_weight")
    var empty_button_text_font_weight: Int = 600

    @SerializedName("empty_past_sub_title_font_size")
    var empty_past_sub_title_font_size: Int = 24

    @SerializedName("empty_past_description_font_size")
    var empty_past_description_font_size: Int = 15

    @SerializedName("empty_past_sub_title_font_weight")
    var empty_past_sub_title_font_weight: Int = 600

    @SerializedName("empty_current_sub_title_font_size")
    var empty_current_sub_title_font_size: Int = 24

    @SerializedName("empty_past_description_font_weight")
    var empty_past_description_font_weight: Int = 400

    @SerializedName("empty_current_description_font_size")
    var empty_current_description_font_size: Int = 15

    @SerializedName("empty_current_sub_title_font_weight")
    var empty_current_sub_title_font_weight: Int = 600

    @SerializedName("empty_current_description_font_weight")
    var empty_current_description_font_weight: Int = 400


//    @SerializedName("ticket_list_empty_icon_color")
//    var ticket_list_empty_icon_color: String = "#9298b1"

//    @SerializedName("ticket_list_empty_text_color")
//    var ticket_list_empty_text_color: String = "#4d4d4d"

//    @SerializedName("ticket_list_empty_current_text")
//    var ticket_list_empty_current_text: String = "You have no resolved support request"

//    @SerializedName("ticket_list_empty_past_text")
//    var ticket_list_empty_past_text: String = "You have no unresolved support request"

//    @SerializedName("ticket_item_shadow_is_hidden")
//    var ticket_item_shadow_is_hidden: Boolean = false
}