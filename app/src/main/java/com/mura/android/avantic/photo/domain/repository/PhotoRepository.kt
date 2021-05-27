package com.mura.android.avantic.photo.domain.repository

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mura.android.avantic.photo.data.response.ResponsePhoto
import com.mura.android.avantic.photo.domain.model.PhotoData
import com.mura.android.avantic.utils.response.ResultManager
import retrofit2.Response

interface PhotoRepository {

    suspend fun getPhotosFromApi(): ResultManager<Response<JsonArray>>
    suspend fun getPhotosFromDB(): ResultManager<List<PhotoData>>
    suspend fun insertPhotosInApi(photoData: ResponsePhoto): ResultManager<Response<JsonObject>>
    suspend fun insertPhotosInDB(photoData: List<PhotoData>): ResultManager<List<Long>>
    suspend fun updatePhotoInApi(photoData: ResponsePhoto): ResultManager<Response<JsonObject>>
    suspend fun updatePhotoInDB(photoData: PhotoData): ResultManager<Int>
    suspend fun deletePhotoByIdToApi(id: String): ResultManager<Response<JsonObject>>
    suspend fun deletePhotoByIdToDB(id: String): ResultManager<Int>
}