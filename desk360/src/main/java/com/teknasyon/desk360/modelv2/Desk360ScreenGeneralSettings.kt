package com.teknasyon.desk360.modelv2

import java.io.Serializable

data class Desk360ScreenGeneralSettings(
    val font_type: String = "Open Sans",
    val add_file_text: String = "Dosya Ekle",
    val name_field_text: String = "İsim",
    val email_field_text: String = "Email",
    val bottom_note_color: String = "#868686",
    val header_icon_color: String = "#5C5C5C",
    val header_text_color: String = "#5C5C5C",
    val copyright_text_color: String = "#ffffff",
    val message_field_text: String = "Mesajınız",
    val subject_field_text: String = "Konu",
    val bottom_note_font_size: Int = 8,
    val header_text_font_size: Int = 20,
    val main_background_color: String = "#F7F7F7",
    val bottom_note_font_weight: Int = 400,
    val header_background_color: String = "#F7F7F7",
    val header_shadow_is_hidden: Boolean = false,
    val header_text_font_weight: Int = 600,
    val copyright_background_color: String = "#71717b",
    val required_field_message: String = "İsim Alanını Doldurunuz.",
    val required_email_field_message: String = "Email Alanını Formatına Göre Giriniz.",
    val required_email_field_message_empty: String = "Email Alanını Doldurunuz.",
    val required_textarea_message: String = "Mesaj Alanını Doldurunuz.",
    val attachment_browse_text: String = "Döküman",
    val attachment_images_text: String = "Resim",
    val attachment_videos_text: String = "Video",
    val file_size_error_text: String = "Dosya Boyutu 20 Mb dan fazla olmamalıdır.",
    val gallery_permission_error_button_text: String = "",
    val attachment_cancel_text: String = "İptal",
    val gallery_permission_error_message: String = ""
) : Serializable
