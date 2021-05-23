package com.mura.android.avantic.photo.domain.usecase

import com.mura.android.avantic.photo.domain.model.Photo
import com.mura.android.avantic.photo.domain.repository.PhotoRepository
import com.mura.android.avantic.utils.response.ResultManager
import javax.inject.Inject

class InsertPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend fun createNewPhotoInApi(photo: Photo) =
        when (val result = photoRepository.insertPhotosInApi(photo)) {
            is ResultManager.Success -> {
                result
            }
            else -> {
                result
            }
        }

    suspend fun saveAllPhotosInDB(listPhoto: List<Photo>) =

        when (val result = photoRepository.insertPhotosInDB(listPhoto)) {
            is ResultManager.Success -> {
                result
            }
            else -> {
                result
            }
        }
}