package com.teknasyon.desk360.model

/**
 * Created by seyfullah on 24,May,2019
 *
 */

class Desk360TicketResponse {
    var id: Int? = null
    var status: String? = null
    var name: String? = null
    var email: String? = null
    var message: String? = null
    var created: String? = null
    var attachment_url : String?= ""
    var messages: ArrayList<Desk360Message>? = null
}