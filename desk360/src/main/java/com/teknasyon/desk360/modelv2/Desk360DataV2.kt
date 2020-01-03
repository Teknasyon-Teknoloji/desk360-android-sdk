package com.teknasyon.desk360.modelv2

class Desk360DataV2 {

    var ticket_detail_screen: Desk360ScreenTicketDetail = Desk360ScreenTicketDetail()

    var create_pre_screen: Desk360ScreenCreatePre = Desk360ScreenCreatePre()

    var ticket_success_screen: Desk360ScreenTicketSuccess = Desk360ScreenTicketSuccess()

    var create_screen: Desk360ScreenCreate = Desk360ScreenCreate()

    var general_settings: Desk360ScreenGeneralSettings = Desk360ScreenGeneralSettings()

    var ticket_list_screen: Desk360ScreenTicketList = Desk360ScreenTicketList()

    var first_screen: Desk360ScreenFirst = Desk360ScreenFirst()

    var custom_fields: ArrayList<Desk360CustomFields>? = null

}