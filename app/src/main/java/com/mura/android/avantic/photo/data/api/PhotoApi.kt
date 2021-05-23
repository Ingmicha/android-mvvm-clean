package com.mura.android.avantic.photo.data.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.mura.android.avantic.photo.domain.model.Photo
import retrofit2.Response
import retrofit2.http.*

interface PhotoApi {

    @GET("/photos")
    suspend fun getPhotos(
    ): Response<JsonArray>

    @POST("/photos")
    suspend fun postPhoto(
        @Body body: Photo
    ): Response<JsonObject>

    @PUT("/photos/{sid}")
    suspend fun putPhoto(
        @Path(value = "id", encoded = true) id: String,
        @Body body: Photo
    ): Response<JsonObject>

    @DELETE("photos/{id}")
    suspend fun deletePhotoById(
        @Path(value = "id", encoded = true) id: String
    ): Response<JsonObject>
}