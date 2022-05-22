package com.niraj.anciarpractical.model

import com.niraj.anciarpractical.model.GeneralizedModel.Companion.TYPE_ITEM

class ModelTypeItem(var headerPosition: Int, var fileUrl: String): GeneralizedModel {
    override fun headerPosition() : Int {
        return headerPosition
    }
    override fun type(): Int {
        return TYPE_ITEM
    }
}