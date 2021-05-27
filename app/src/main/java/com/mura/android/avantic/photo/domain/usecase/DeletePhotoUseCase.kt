package com.mura.android.avantic.photo.domain.usecase

import com.mura.android.avantic.photo.domain.repository.PhotoRepository
import com.mura.android.avantic.utils.extentions.NetworkHelper
import com.mura.android.avantic.utils.response.ResultManager
import javax.inject.Inject

class DeletePhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend fun deletePhotoInApi(hasNetwork: Boolean, id: String) =
        if (hasNetwork) {
            when (val result = (photoRepository.deletePhotoByIdToApi(id))) {
                is ResultManager.Success -> {
                    if (result.items!!.isSuccessful) {
                        result
                    } else {
                        ResultManager.Error(
                            result.items.code(),
                            result.items.errorBody().toString()
                        )
                    }
                }
                else -> {
                    result
                }
            }
        } else {
            ResultManager.Error(0, NetworkHelper.NO_CONNECTED)
        }

    suspend fun deletePhotoInDB(id: String) =
        when (val result = (photoRepository.deletePhotoByIdToDB(id))) {
            is ResultManager.Success -> {
                result
            }
            else -> {
                result
            }
        }

}