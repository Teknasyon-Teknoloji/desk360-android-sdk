package com.teknasyon.desk360.modelv2

data class Desk360CustomFields(
    val id: Int? = null,
    val name: String? = null,
    val type: String? = null,
    val options: ArrayList<Desk360Options>? = null,
    val place_holder: String? = null
)