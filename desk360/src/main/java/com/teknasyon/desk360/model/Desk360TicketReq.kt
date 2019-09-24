package com.teknasyon.desk360.model

/**
 * Created by seyfullah on 24,May,2019
 *
 */

data class Desk360TicketReq(
    var name: String? = null,
    var email: String? = null,
//    var subject: String? = null,
    var message: String? = null,
    var type_id: String? = null,
    var source: String? = null,
    var platform: String? = null,
    var country_code: String? = null
)