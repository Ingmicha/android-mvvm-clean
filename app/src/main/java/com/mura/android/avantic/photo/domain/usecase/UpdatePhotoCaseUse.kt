package com.mura.android.avantic.photo.domain.usecase

import com.mura.android.avantic.photo.data.response.ResponsePhoto
import com.mura.android.avantic.photo.domain.model.PhotoData
import com.mura.android.avantic.photo.domain.repository.PhotoRepository
import com.mura.android.avantic.utils.extentions.NetworkHelper
import com.mura.android.avantic.utils.response.ResultManager
import javax.inject.Inject

class UpdatePhotoCaseUse @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend fun updatePhotoInApi(hasNetwork: Boolean, photoData: ResponsePhoto) =
        if (hasNetwork) {
            when (val result = photoRepository.updatePhotoInApi(photoData)) {
                is ResultManager.Success -> {
                    result
                }
                else -> {
                    result
                }
            }
        } else {
            ResultManager.Error(0, NetworkHelper.NO_CONNECTED)
        }

    suspend fun updatePhotoInDB(photoData: PhotoData) =
        when (val result = photoRepository.updatePhotoInDB(photoData)) {
            is ResultManager.Success -> {
                result
            }
            else -> {
                result
            }
        }

}