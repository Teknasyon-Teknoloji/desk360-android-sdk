package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Desk360ScreenGeneralSettings : Serializable {

    @SerializedName("font_type")
    var font_type: String = "Open Sans"

    @SerializedName("header_background_color")
    var header_background_color: String = "#f7f7f7"

    @SerializedName("header_shadow_is_hidden")
    var header_shadow_is_hidden: Boolean = false

    @SerializedName("header_icon_color")
    var header_icon_color: String = "#5c5c5c"

    @SerializedName("bottom_note_color")
    var bottom_note_color: String = "#868686"

    @SerializedName("bottom_note_font_weight")
    var bottom_note_font_weight: Int = 400

    @SerializedName("header_text_color")
    var header_text_color: String = "#5c5c5c"

    @SerializedName("header_text_font_weight")
    var header_text_font_weight: Int = 600

    @SerializedName("header_text_font_size")
    var header_text_font_size: Int = 20

    @SerializedName("main_background_color")
    var main_background_color: String = "#3BF761"

    @SerializedName("copyright_text_color")
    var copyright_text_color: String = "#ffffff"

    @SerializedName("bottom_note_font_size")
    var bottom_note_font_size: Int = 8

    @SerializedName("copyright_background_color")
    var copyright_background_color: String = "#71717b"
}