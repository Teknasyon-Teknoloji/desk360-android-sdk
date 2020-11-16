package com.teknasyon.desk360.modelv2

data class Desk360ScreenTicketList(
    val title: String = "Destek Taleplerim",
    val tab_past_text: String = "Geçmiş",
    val tab_text_color: String = "#9298B1",
    val tab_border_color: String = "#f7f7f7",
    val tab_current_text: String = "Mevcut",
    val ticket_list_type: Int = 1,
    val ticket_date_color: String = "#B0B0B0",
    val tab_text_font_size: Int = 18,
    val tab_text_font_weight: Int = 600,
    val ticket_subject_color: String = "#2D2D2D",
    val tab_text_active_color: String = "#58B0FA",
    val ticket_date_font_size: Int = 12,
    val ticket_item_icon_color: String = "#58B0FA",
    val tab_active_border_color: String = "#58B0FA",
    val ticket_subject_font_size: Int = 16,
    val ticket_item_backgroud_color: String = "#FFFFFF",
    val ticket_list_backgroud_color: String = "#EEEFF0",
    val ticket_list_empty_past_text: String = "Çözülmemiş destek talebiniz bulunmamaktadır",
    val ticket_item_shadow_is_hidden: Boolean = false,
    val ticket_list_empty_icon_color: String = "#9298B1",
    val ticket_list_empty_text_color: String = "#4D4D4D",
    val ticket_list_empty_current_text: String = "Çözülmüş destek talebiniz bulunmamaktadır"
)

