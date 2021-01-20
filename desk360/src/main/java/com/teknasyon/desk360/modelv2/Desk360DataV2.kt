package com.teknasyon.desk360.modelv2

data class Desk360DataV2(
    val ticket_detail_screen: Desk360ScreenTicketDetail? = null,
    val create_pre_screen: Desk360ScreenCreatePre? = null,
    val ticket_success_screen: Desk360ScreenTicketSuccess? = null,
    val create_screen: Desk360ScreenCreate? = null,
    val general_settings: Desk360ScreenGeneralSettings? = null,
    val ticket_list_screen: Desk360ScreenTicketList? = null,
    val first_screen: Desk360ScreenFirst? = null,
    val custom_fields: ArrayList<Desk360CustomFields>? = null
)