package com.teknasyon.desk360.model

import android.view.View

/**
 * Created by seyfullah on 24,May,2019
 *
 */

class Desk360Message {
    var id: Int? = null
    var message: String? = null
    var created: String? = null
    var is_answer: Boolean = false
    var attachments: Desk360Attachment? = null
    var tick: Boolean = true
    @Transient
    var views: List<View>? = null
}