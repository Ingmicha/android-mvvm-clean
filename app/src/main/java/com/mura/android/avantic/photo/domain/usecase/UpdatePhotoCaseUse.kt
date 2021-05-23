package com.mura.android.avantic.photo.domain.usecase

import com.mura.android.avantic.photo.domain.model.Photo
import com.mura.android.avantic.photo.domain.repository.PhotoRepository
import com.mura.android.avantic.utils.response.ResultManager
import javax.inject.Inject

class UpdatePhotoCaseUse @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend fun updatePhotoInApi(photo: Photo) =
        when (val result = photoRepository.updatePhotoInApi(photo)) {
            is ResultManager.Success -> {
                result
            }
            else -> {
                result
            }
        }

    suspend fun updatePhotoInDB(photo: Photo) =
        when (val result = photoRepository.insertPhotosInApi(photo)) {
            is ResultManager.Success -> {
                result
            }
            else -> {
                result
            }
        }

}