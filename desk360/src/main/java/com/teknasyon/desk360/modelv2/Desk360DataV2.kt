package com.teknasyon.desk360.modelv2

data class Desk360DataV2(
    val ticket_detail_screen: Desk360ScreenTicketDetail = Desk360ScreenTicketDetail(),
    val create_pre_screen: Desk360ScreenCreatePre = Desk360ScreenCreatePre(),
    val ticket_success_screen: Desk360ScreenTicketSuccess = Desk360ScreenTicketSuccess(),
    val create_screen: Desk360ScreenCreate = Desk360ScreenCreate(),
    val general_settings: Desk360ScreenGeneralSettings = Desk360ScreenGeneralSettings(),
    val ticket_list_screen: Desk360ScreenTicketList = Desk360ScreenTicketList(),
    val first_screen: Desk360ScreenFirst = Desk360ScreenFirst(),
    val custom_fields: List<Desk360CustomFields>? = null
)