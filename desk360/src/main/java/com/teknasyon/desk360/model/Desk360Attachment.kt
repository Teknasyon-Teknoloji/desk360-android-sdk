package com.teknasyon.desk360.model

data class Desk360Attachment(
    val files: List<Desk360File>,
    val images: List<Desk360File>,
    val others: List<Desk360File>,
    val videos: List<Desk360File>
)