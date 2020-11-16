package com.teknasyon.desk360.modelv2

import com.teknasyon.desk360.model.Desk360Type

data class Desk360ScreenCreate(
    val title: String = "Bize Ulaşın",
    val button_text: String = "Gönder",
    val form_style_id: Int = 1,
    val button_style_id: Int = 1,
    val bottom_note_text: String =
        "Açık bir destek talebiniz bulunmaktadır. Talebiniz henüz yanıtlanmadıysa lütfen beklemeye devam ediniz. Tekrar iletişime geçmek isterseniz aynı talep üzerinden bize tekrar yazabilirsiniz",
    val form_input_color: String = "#A6A6A8",
    val label_text_color: String = "#2E86CF",
    val button_text_color: String = "#FFFFFF",
    val button_border_color: String = "#58B0FA",
    val added_file_is_hidden: Boolean = false,
    val form_input_font_size: Int = 18,
    val label_text_font_size: Int = 14,
    val bottom_note_is_hidden: Boolean = false,
    val button_icon_is_hidden: Boolean = false,
    val button_text_font_size: Int = 18,
    val error_label_text_color: String = "#FF0000",
    val form_input_focus_color: String = "#000000",
    val form_input_font_weight: Int = 400,
    val label_text_font_weight: Int = 400,
    val button_background_color: String = "#58B0FA",
    val button_shadow_is_hidden: Boolean = false,
    val button_text_font_weight: Int = 600,
    val form_input_border_color: String = "#BFBFC1",
    val form_input_background_color: String = "#F7F7F7",
    val form_input_focus_border_color: String = "#000000",
    val form_input_place_holder_color: String = "#a6a6a8",
    val form_input_focus_background_color: String = "#F7F7F7",
    val types: List<Desk360Type>? = null
)