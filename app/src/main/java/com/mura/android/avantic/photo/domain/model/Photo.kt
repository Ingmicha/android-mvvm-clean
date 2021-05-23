package com.mura.android.avantic.photo.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "photo",
    indices = [Index(value = ["id"], unique = true)]
)

data class Photo(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @Expose
    @ColumnInfo(name = "albumId")
    @SerializedName("albumId")
    val albumId: String = "",

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String = "",

    @ColumnInfo(name = "url")
    @SerializedName("url")
    val url: String = "",

    @ColumnInfo(name = "thumbnailUrl")
    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String = ""
)