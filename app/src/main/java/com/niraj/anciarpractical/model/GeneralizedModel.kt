package com.niraj.anciarpractical.model

interface GeneralizedModel {
    fun type(): Int
    fun headerPosition(): Int
    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }
}