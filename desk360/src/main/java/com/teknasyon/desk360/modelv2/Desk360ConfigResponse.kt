package com.teknasyon.desk360.modelv2

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Desk360ConfigResponse : Serializable {

    @SerializedName("meta")
    var meta: Desk360MetaV2 = Desk360MetaV2()
    @SerializedName("data")
    var data: Desk360DataV2 = Desk360DataV2()
}