package com.teknasyon.desk360.modelv2

data class Desk360ScreenFirst(
    val title: String = "Bize Ulaşın",
    val sub_title: String = "Size nasıl yardımcı olabiliriz?",
    val button_text: String = "Bize Yazın",
    val description: String = "İlk yardım mesajını yaratmak ve canlı destek almak için bize ulaşın!",
    val button_style_id: Int = 1,
    val sub_title_color: String = "#4D4D4D",
    val bottom_note_text: String =
        "Açık bir destek talebiniz bulunmaktadır. Talebiniz henüz yanıtlanmadıysa lütfen beklemeye devam ediniz. Tekrar iletişime geçmek isterseniz aynı talep üzerinden bize tekrar yazabilirsiniz",
    val button_text_color: String = "#FFFFFF",
    val description_color: String = "#868686",
    val button_border_color: String = "#58B0FA",
    val sub_title_font_size: Int = 24,
    val bottom_note_is_hidden: Boolean = false,
    val button_icon_is_hidden: Boolean = false,
    val button_text_font_size: Int = 18,
    val description_font_size: Int = 15,
    val sub_title_font_weight: Int = 600,
    val button_background_color: String = "#58b0fa",
    val button_shadow_is_hidden: Boolean = false,
    val button_text_font_weight: Int = 600,
    val description_font_weight: Int = 400
)