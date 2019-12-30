package com.teknasyon.desk360.modelv2

import java.io.Serializable

class Desk360ScreenGeneralSettings : Serializable {

    var font_type = "Open Sans"
    var add_file_text = "Dosya Ekle"
    var name_field_text = "İsim"
    var email_field_text =  "Email"
    var bottom_note_color = "#868686"
    var header_icon_color = "#5C5C5C"
    var header_text_color = "#5C5C5C"
    var copyright_text_color = "#ffffff"
    var message_field_text=  "Mesajınız"
    var subject_field_text =  "Konu"
    var bottom_note_font_size = 8
    var header_text_font_size = 20
    var main_background_color = "#F7F7F7"
    var bottom_note_font_weight = 400
    var header_background_color = "#F7F7F7"
    var header_shadow_is_hidden = false
    var header_text_font_weight = 600
    var copyright_background_color = "#71717b"

    var required_field_message = "İsim Alanını Doldurunuz."
    var required_email_field_message = "Email Alanını Formatına Göre Giriniz."
    var required_email_field_message_empty = "Email Alanını Doldurunuz."
    var required_textarea_message = "Mesaj Alanını Doldurunuz."

    var attachment_browse_text = "Döküman"
    var attachment_images_text = "Resim"
    var attachment_videos_text = "Video"
    var file_size_error_text = "Dosya Boyutu 20 Mb dan fazla olmamalıdır."

    var gallery_permission_error_button_text = ""
    var attachment_cancel_text = "İptal"
    var gallery_permission_error_message = ""

}
