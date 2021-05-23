package com.mura.android.avantic.photo.data.repository

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mura.android.avantic.photo.data.api.PhotoApiDataSource
import com.mura.android.avantic.photo.data.database.PhotoDao
import com.mura.android.avantic.photo.domain.model.Photo
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
                ResultManager.Success(
                    dataSource.getPhotos()
                )
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun getPhotosFromDB(): ResultManager<List<Photo>> {
        return safeApiCall(
            call = {
                ResultManager.Success(
                    dao.selectAll()
                )
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun insertPhotosInApi(photo: Photo): ResultManager<Response<JsonObject>> {
        return safeApiCall(
            call = {
                ResultManager.Success(
                    dataSource.postPhoto(photo)
                )
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun insertPhotosInDB(photo: List<Photo>): ResultManager<List<Long>> {
        return safeApiCall(
            call = {
                ResultManager.Success(
                    dao.insertAll(photo)
                )
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun updatePhotoInApi(photo: Photo): ResultManager<Response<JsonObject>> {
        return safeApiCall(
            call = {
                ResultManager.Success(
                    dataSource.putPhoto(photo)
                )
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun updatePhotoInDB(photo: Photo): ResultManager<Int> {
        return safeApiCall(
            call = {
                ResultManager.Success(
                    dao.updateTitleById(photo.title, photo.id)
                )
            },
            errorMessage = "Exception occurred!"
        )
    }

    override suspend fun deletePhotoByIdToApi(id: String): ResultManager<Response<JsonObject>> {
        return safeApiCall(
            call = {
                ResultManager.Success(
                    dataSource.deletePhotoById(id)
                )
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