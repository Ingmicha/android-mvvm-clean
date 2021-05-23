package com.mura.android.avantic.photo.domain.usecase

import com.mura.android.avantic.photo.domain.repository.PhotoRepository
import com.mura.android.avantic.utils.response.ResultManager
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(
    private val PhotoRepository: PhotoRepository
) {

    suspend fun getPhotoFromApi() =
        when (val result = (PhotoRepository.getPhotosFromApi())) {
            is ResultManager.Success -> {
                if (result.items!!.isSuccessful) {
                    result
                } else {
                    ResultManager.Error(result.items.code(), result.items.errorBody().toString())
                }
            }
            else -> {
                result
            }
        }

    suspend fun getPhotoFromDB() =
        when (val result = (PhotoRepository.getPhotosFromDB())) {
            is ResultManager.Success -> {
                result
            }
            else -> {
                result
            }
        }
}