package com.teknasyon.desk360.model

/**
 * Created by seyfullah on 24,May,2019
 *
 */

data class Desk360TicketResponse(
    val id: Int? = null,
    val status: String? = null,
    val name: String? = null,
    val email: String? = null,
    val message: String? = null,
    val created: String? = null,
    val attachment_url: String? = "",
    val messages: ArrayList<Desk360Message>? = null
)