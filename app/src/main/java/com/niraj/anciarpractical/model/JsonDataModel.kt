package com.niraj.anciarpractical.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class JsonDataModel(

	@field:SerializedName("data")
	val jsonDataModelItem: ArrayList<JsonDataModelItem>
) : Parcelable

@Parcelize
data class JsonDataModelItem(

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("imageList")
	val imageList: ArrayList<String>
) : Parcelable
