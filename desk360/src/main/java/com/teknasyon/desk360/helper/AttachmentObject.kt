package com.teknasyon.desk360.helper

import com.teknasyon.desk360.view.adapter.Desk360TicketDetailListAdapter

class AttachmentObject {

    var position: Int? = null
    var view: Desk360TicketDetailListAdapter.ViewHolder? = null

    override fun toString(): String {
        return "AttachmentObject(position=$position, view=$view)"
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AttachmentObject

        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        return position ?: 0
    }
}


