package com.mura.android.avantic.photo.domain.usecase

import com.google.gson.Gson
import com.mura.android.avantic.photo.data.response.ResponsePhoto
import com.mura.android.avantic.photo.domain.model.PhotoData
import com.mura.android.avantic.photo.domain.repository.PhotoRepository
import com.mura.android.avantic.utils.extentions.NetworkHelper
import com.mura.android.avantic.utils.response.ResultManager
import javax.inject.Inject

class InsertPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val gson: Gson
) {

    suspend fun createNewPhotoInApi(
        hasNetwork: Boolean,
        photoData: ResponsePhoto
    ): ResultManager<Any> =
        if (hasNetwork) {
            when (val result = photoRepository.insertPhotosInApi(photoData)) {
                is ResultManager.Success -> {
                    val photoMapperToDB =
                        gson.fromJson(result.items?.body().toString(), PhotoData::class.java)
                    ResultManager.Success(photoMapperToDB)
                }
                else -> {
                    result
                }
            }
        } else {
            ResultManager.Error(0, NetworkHelper.NO_CONNECTED)
        }

    suspend fun saveAllPhotosInDB(listPhotoData: List<PhotoData>) =

        when (val result = photoRepository.insertPhotosInDB(listPhotoData)) {
            is ResultManager.Success -> {
                result
            }
            else -> {
                result
            }
        }
}