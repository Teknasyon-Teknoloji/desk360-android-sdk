package com.teknasyon.desk360.model

/**
 * Created by seyfullah on 24,May,2019
 *
 */

data class Desk360Message(
    val id: Int? = null,
    val message: String? = null,
    val created: String? = null,
    val is_answer: Boolean? = false,
    val tick: Boolean? = true
)