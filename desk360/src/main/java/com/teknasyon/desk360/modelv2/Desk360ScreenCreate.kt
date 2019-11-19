package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Desk360ScreenCreate : Serializable {

    @SerializedName("button_background_color")
    var button_background_color: String = "#58b0fa"

    @SerializedName("bottom_note_is_hidden")
    var bottom_note_is_hidden: Boolean = false

    @SerializedName("button_icon_is_hidden")
    var button_icon_is_hidden: Boolean = true

//    @SerializedName("button_shadow_is_hidden")
//    var button_shadow_is_hidden: Boolean = false

    @SerializedName("form_input_font_size")
    var form_input_font_size: Int = 18

    @SerializedName("form_input_font_weight")
    var form_input_font_weight: String = "400"

    @SerializedName("label_text_color")
    var label_text_color: String = "#2e86cf"

    @SerializedName("label_text_font_size")
    var label_text_font_size: Int = 14

    @SerializedName("label_text_font_weight")
    var label_text_font_weight: Int = 400


    @SerializedName("form_input_focus_border_color")
    var form_input_focus_border_color: String = "#000000"

    @SerializedName("error_label_text_color")
    var error_label_text_color: String = "#ff0000"


    @SerializedName("bottom_note_text")
    var bottom_note_text: String =
        "Destek mesajlarınız mesai saatleri içerisinde yanıtlar. Buraya buna benzer bir içerik mesajı ve alt kısa açıklama eklenir." +
                " Geliştirici istediği mesajı burada verebilir. Kullanılması zorunlu değildir." +
                " Destek mesajlarınız mesai saatleri içerisinde yanıtlar"

    @SerializedName("form_input_place_holder_color")
    var form_input_place_holder_color: String = "#a6a6a8"

    @SerializedName("form_input_border_color")
    var form_input_border_color: String = "#bfbfc1"

    @SerializedName("form_input_background_color")
    var form_input_background_color: String = "#f7f7f7"

    @SerializedName("button_text_font_size")
    var button_text_font_size: Int = 18

    @SerializedName("title")
    var title: String = "Bize Ulaşın"

    @SerializedName("form_input_focus_color")
    var form_input_focus_color: String = "#000000"

    @SerializedName("button_text_font_weight")
    var button_text_font_weight: Int = 600

    @SerializedName("added_file_is_hidden")
    var added_file_is_hidden: Boolean = false

    @SerializedName("form_input_color")
    var form_input_color: String = "#a6a6a8"

    @SerializedName("button_text_color")
    var button_text_color: String = "#ffffff"

    @SerializedName("form_style_id")
    var form_style_id: Int = 1

    @SerializedName("button_style_id")
    var button_style_id: Int = 1

    @SerializedName("form_input_focus_background_color")
    var form_input_focus_background_color: String = "#f7f7f7"

    @SerializedName("button_text")
    var button_text: String = "Gönder"

    @SerializedName("button_border_color")
    var button_border_color: String = "#58b0fa"
}