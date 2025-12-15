package com.teknasyon.desk360.modelv2

data class Desk360ConfigResponse(
    val meta: Desk360MetaV2? = Desk360MetaV2(),
    val data: Desk360DataV2? = Desk360DataV2()
)