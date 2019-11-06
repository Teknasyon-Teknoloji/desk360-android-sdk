package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Desk360ScreenGeneralSettings : Serializable {

    @SerializedName("header_background_color")
    var header_background_color: String = "#cecece"

    @SerializedName("header_shadow")
    var header_shadow: String = "TEST_DATA"

    @SerializedName("header_icon_color")
    var header_icon_color: String = "#000000"

    @SerializedName("bottom_note_color")
    var bottom_note_color: String = "#868686"

    @SerializedName("bottom_note_font_weight")
    var bottom_note_font_weight: String = "regular"

    @SerializedName("header_text_color")
    var header_text_color: String = "#000000"

    @SerializedName("header_text_font_weight")
    var header_text_font_weight: String = "regular"

    @SerializedName("header_text_font_size")
    var header_text_font_size: Int = 15

    @SerializedName("main_background_color")
    var main_background_color: String = "#cecece"

    @SerializedName("copyright_text_color")
    var copyright_text_color: String = "#ffffff"

    @SerializedName("bottom_note_font_size")
    var bottom_note_font_size: Int = 8

    @SerializedName("copyright_background_color")
    var copyright_background_color: String = "#71717b"
}