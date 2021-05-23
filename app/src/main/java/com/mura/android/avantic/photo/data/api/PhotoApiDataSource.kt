package com.mura.android.avantic.photo.data.api

import com.mura.android.avantic.photo.domain.model.Photo
import javax.inject.Inject

class PhotoApiDataSource @Inject constructor(
    private val photoApi: PhotoApi
) {

    suspend fun getPhotos() =
        photoApi.getPhotos()

    suspend fun postPhoto(photo: Photo) =
        photoApi.postPhoto(photo)

    suspend fun putPhoto(photo: Photo) =
        photoApi.postPhoto(photo)

    suspend fun deletePhotoById(id: String) =
        photoApi.deletePhotoById(id)

}