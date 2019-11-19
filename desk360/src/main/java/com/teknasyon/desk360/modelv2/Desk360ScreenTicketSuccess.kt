package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Desk360ScreenTicketSuccess : Serializable {

    @SerializedName("sub_title_color")
    var sub_title_color: String = "#4d4d4d"

    @SerializedName("button_background_color")
    var button_background_color: String = "#58b0fa"

    @SerializedName("bottom_note_is_hidden")
    var bottom_note_is_hidden: Boolean = false

//    @SerializedName("button_shadow_is_hidden")
//    var button_shadow_is_hidden: Boolean = false

    @SerializedName("button_icon_is_hidden")
    var button_icon_is_hidden: Boolean = true

    @SerializedName("bottom_note_text")
    var bottom_note_text: String =
        "DDestek mesajlarınız mesai saatleri içerisinde yanıtlar. Buraya buna benzer bir içerik mesajı ve alt kısa açıklama eklenir." +
                " Geliştirici istediği mesajı burada verebilir." +
                " Kullanılması zorunlu değildir. Destek mesajlarınız mesai saatleri içerisinde yanıtlar"

    @SerializedName("sub_title")
    var sub_title: String = "Teşekkürler"

    @SerializedName("button_text_font_size")
    var button_text_font_size: Int = 18

    @SerializedName("description")
    var description: String =
        "Mesajınızı aldık. En kısa sürede bir uzman mesajınıza yanıt gönderecektir"

    @SerializedName("description_font_size")
    var description_font_size: Int = 15

    @SerializedName("title")
    var title: String = "Bize Ulaşın"

    @SerializedName("description_color")
    var description_color: String = "#868686"

    @SerializedName("sub_title_font_size")
    var sub_title_font_size: Int = 24

    @SerializedName("button_text_font_weight")
    var button_text_font_weight: Int = 600

    @SerializedName("icon_color")
    var icon_color: String = "#0ebc98"

    @SerializedName("button_text_color")
    var button_text_color: String = "#ffffff"

    @SerializedName("icon_size")
    var icon_size: Int? = 80

    @SerializedName("button_style_id")
    var button_style_id: Int = 1

    @SerializedName("sub_title_font_weight")
    var sub_title_font_weight: Int = 600

    @SerializedName("button_text")
    var button_text: String = "Mesajlar Sayfasına Git"

    @SerializedName("button_border_color")
    var button_border_color: String = "#58b0fa"

    @SerializedName("description_font_weight")
    var description_font_weight: Int = 400

}