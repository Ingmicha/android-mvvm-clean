package com.mura.android.avantic.photo.domain.usecase

import com.google.gson.Gson
import com.mura.android.avantic.photo.data.mapper.PhotoMapperToDB
import com.mura.android.avantic.photo.domain.model.PhotoData
import com.mura.android.avantic.photo.domain.repository.PhotoRepository
import com.mura.android.avantic.utils.response.ResultManager
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(
    private val PhotoRepository: PhotoRepository,
    private val gson: Gson
) {

    suspend fun getPhotos(hasNetwork: Boolean): ResultManager<Any> {
        if (hasNetwork) {
            return when (val result = (PhotoRepository.getPhotosFromApi())) {
                is ResultManager.Success -> {
                    val photoMapperToDB =
                        gson.fromJson(result.items?.body().toString(), Array<PhotoData>::class.java)
                    ResultManager.Success(photoMapperToDB)
                }
                is ResultManager.Error -> {
                    result
                }
                else -> {
                    result
                }
            }
        } else {
            return getPhotosFromBD()
        }
    }

    suspend fun getPhotosFromBD(): ResultManager<Any> {
        return when (val result = (PhotoRepository.getPhotosFromDB())) {
            is ResultManager.Success -> {
                ResultManager.Success(result.items)
            }
            else -> {
                result
            }
        }
    }

}