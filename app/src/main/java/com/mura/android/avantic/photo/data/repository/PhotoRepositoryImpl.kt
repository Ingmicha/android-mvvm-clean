package com.mura.android.avantic.photo.data.repository

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mura.android.avantic.photo.data.api.PhotoApiDataSource
import com.mura.android.avantic.photo.data.database.PhotoDao
import com.mura.android.avantic.photo.data.response.ResponsePhoto
import com.mura.android.avantic.photo.domain.model.PhotoData
import com.mura.android.avantic.photo.domain.repository.PhotoRepository
import com.mura.android.avantic.utils.extentions.safeApiCall
import com.mura.android.avantic.utils.response.ResultManager
import retrofit2.Response
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val dataSource: PhotoApiDataSource,
    private val dao: PhotoDao
) : PhotoRepository {

    override suspend fun getPhotosFromApi(): ResultManager<Response<JsonArray>> {
        return safeApiCall(
            call = {
                val response = dataSource.getPhotos()
                if (response.isSuccessful) {
                    ResultManager.Success(response)
                } else {
                    ResultManager.Error(response.code(), response.errorBody().toString())
                }
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun getPhotosFromDB(): ResultManager<List<PhotoData>> {
        return safeApiCall(
            call = {
                ResultManager.Success(
                    dao.selectAll()
                )
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun insertPhotosInApi(photoData: ResponsePhoto): ResultManager<Response<JsonObject>> {
        return safeApiCall(
            call = {
                val response = dataSource.postPhoto(photoData)
                if (response.isSuccessful) {
                    ResultManager.Success(response)
                } else {
                    ResultManager.Error(response.code(), response.errorBody().toString())
                }
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun insertPhotosInDB(photoData: List<PhotoData>): ResultManager<List<Long>> {
        return safeApiCall(
            call = {
                ResultManager.Success(
                    dao.insertAll(photoData)
                )
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun updatePhotoInApi(photoData: ResponsePhoto): ResultManager<Response<JsonObject>> {
        return safeApiCall(
            call = {
                val response = dataSource.putPhoto(photoData)
                if (response.isSuccessful) {
                    ResultManager.Success(response)
                } else {
                    ResultManager.Error(response.code(), response.errorBody().toString())
                }
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun updatePhotoInDB(photoData: PhotoData): ResultManager<Int> {
        return safeApiCall(
            call = {
                ResultManager.Success(
                    dao.updateTitleById(photoData.title, photoData.id)
                )
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun deletePhotoByIdToApi(id: String): ResultManager<Response<JsonObject>> {
        return safeApiCall(
            call = {
                val response = dataSource.deletePhotoById(id)
                if (response.isSuccessful) {
                    ResultManager.Success(response)
                } else {
                    ResultManager.Error(response.code(), response.errorBody().toString())
                }
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun deletePhotoByIdToDB(id: String): ResultManager<Int> {
        return safeApiCall(
            call = {
                ResultManager.Success(
                    dao.deletePhoto(id)
                )
            },
            errorMessage = "Exception occurred!"
        )
    }

}