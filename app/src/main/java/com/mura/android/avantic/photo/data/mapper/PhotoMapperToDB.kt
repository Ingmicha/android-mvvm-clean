package com.mura.android.avantic.photo.data.mapper

import com.mura.android.avantic.photo.data.response.ResponsePhoto
import com.mura.android.avantic.photo.domain.model.PhotoData
import com.mura.android.avantic.utils.database.Mapper
import java.lang.UnsupportedOperationException

class PhotoMapperToDB : Mapper<PhotoData, ResponsePhoto>() {
    override fun map(value: PhotoData): ResponsePhoto {
        throw UnsupportedOperationException()
    }

    override fun reverseMap(value: ResponsePhoto): PhotoData {
        return PhotoData(
            id = value.id.toInt(),
            title = value.title,
            url = value.url
        )
    }
}