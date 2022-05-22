package com.niraj.anciarpractical.model

import com.niraj.anciarpractical.model.GeneralizedModel.Companion.TYPE_HEADER

class ModelTypeHeader(var headerPosition: Int, var title: String): GeneralizedModel {
    override fun headerPosition() : Int {
        return headerPosition
    }
    override fun type(): Int {
        return TYPE_HEADER
    }
}