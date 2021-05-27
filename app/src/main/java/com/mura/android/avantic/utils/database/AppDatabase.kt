package com.mura.android.avantic.utils.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mura.android.avantic.photo.data.database.PhotoDao
import com.mura.android.avantic.photo.domain.model.PhotoData

@Database(entities = [PhotoData::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photoDao(): PhotoDao
}