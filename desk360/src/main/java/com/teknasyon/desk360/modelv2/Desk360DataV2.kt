package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Desk360DataV2 : Serializable  {

    @SerializedName("ticket_detail_screen")
    var ticket_detail_screen: Desk360ScreenTicketDetail? = null

    @SerializedName("create_pre_screen")
    var create_pre_screen: Desk360ScreenCreatePre? = null

    @SerializedName("ticket_success_screen")
    var ticket_success_screen: Desk360ScreenTicketSuccess? = null

    @SerializedName("create_screen")
    var create_screen: Desk360ScreenCreate? = null

    @SerializedName("general_settings")
    var general_settings: Desk360ScreenGeneralSettings? = null

    @SerializedName("ticket_list_screen")
    var ticket_list_screen: Desk360ScreenTicketList? = null

    @SerializedName("first_screen")
    var first_screen: Desk360ScreenFirst? = null

}