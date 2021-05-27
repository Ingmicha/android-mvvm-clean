package com.mura.android.avantic.photo.data.api

import com.mura.android.avantic.photo.data.response.ResponsePhoto
import javax.inject.Inject

class PhotoApiDataSource @Inject constructor(
    private val photoApi: PhotoApi
) {

    suspend fun getPhotos() =
        photoApi.getPhotos()

    suspend fun postPhoto(photoData: ResponsePhoto) =
        photoApi.postPhoto(photoData)

    suspend fun putPhoto(photoData: ResponsePhoto) =
        photoApi.putPhoto(photoData.id, photoData)

    suspend fun deletePhotoById(id: String) =
        photoApi.deletePhotoById(id)

}