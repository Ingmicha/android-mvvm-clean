package com.mura.android.avantic.photo.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mura.android.avantic.photo.domain.model.PhotoData

@Dao
interface PhotoDao {

    @Query("SELECT * FROM photo ORDER BY id ASC")
    suspend fun selectAll(): List<PhotoData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photoData: List<PhotoData>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photoData: PhotoData): Long

    @Query("UPDATE photo SET title = :title where id=:id")
    suspend fun updateTitleById(title: String, id: Int): Int

    @Query("DELETE FROM photo WHERE id =:id")
    suspend fun deletePhoto(id: String): Int

}
