package com.mura.android.avantic.photo.data.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponsePhoto(
    @Expose
    @SerializedName("id")
    val id: String = "",
    @Expose
    @SerializedName("albumId")
    val albumId: String = "",
    @Expose
    @SerializedName("title")
    var title: String = "",
    @Expose
    @SerializedName("url")
    var url: String = "",
    @Expose
    @SerializedName("thumbnailUrl")
    var thumbnailUrl: String = ""
)
