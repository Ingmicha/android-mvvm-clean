package com.mura.android.avantic.photo.domain.repository

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mura.android.avantic.photo.domain.model.Photo
import com.mura.android.avantic.utils.response.ResultManager
import retrofit2.Response

interface PhotoRepository {

    suspend fun getPhotosFromApi(): ResultManager<Response<JsonArray>>
    suspend fun getPhotosFromDB(): ResultManager<List<Photo>>
    suspend fun insertPhotosInApi(photo: Photo): ResultManager<Response<JsonObject>>
    suspend fun insertPhotosInDB(photo: List<Photo>): ResultManager<List<Long>>
    suspend fun updatePhotoInApi(photo: Photo): ResultManager<Response<JsonObject>>
    suspend fun updatePhotoInDB(photo: Photo): ResultManager<Int>
    suspend fun deletePhotoByIdToApi(id: String): ResultManager<Response<JsonObject>>
    suspend fun deletePhotoByIdToDB(id: String): ResultManager<Int>
}