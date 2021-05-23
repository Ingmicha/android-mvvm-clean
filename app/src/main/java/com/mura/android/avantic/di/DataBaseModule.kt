package com.mura.android.avantic.di

import android.content.Context
import androidx.room.Room
import com.mura.android.avantic.photo.data.database.PhotoDao
import com.mura.android.avantic.utils.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "db_app"
        ).build()
    }

    @Provides
    fun provideLyricDao(appDatabase: AppDatabase): PhotoDao {
        return appDatabase.photoDao()
    }

}