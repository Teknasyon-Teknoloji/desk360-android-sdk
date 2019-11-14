package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Desk360DataV2 : Serializable {

    @SerializedName("ticket_detail_screen")
    var ticket_detail_screen: Desk360ScreenTicketDetail = Desk360ScreenTicketDetail()

    @SerializedName("create_pre_screen")
    var create_pre_screen: Desk360ScreenCreatePre = Desk360ScreenCreatePre()

    @SerializedName("ticket_success_screen")
    var ticket_success_screen: Desk360ScreenTicketSuccess = Desk360ScreenTicketSuccess()

    @SerializedName("create_screen")
    var create_screen: Desk360ScreenCreate = Desk360ScreenCreate()

    @SerializedName("general_settings")
    var general_settings: Desk360ScreenGeneralSettings = Desk360ScreenGeneralSettings()

    @SerializedName("ticket_list_screen")
    var ticket_list_screen: Desk360ScreenTicketList = Desk360ScreenTicketList()

    @SerializedName("first_screen")
    var first_screen: Desk360ScreenFirst = Desk360ScreenFirst()

}