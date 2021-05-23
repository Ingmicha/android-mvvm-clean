package com.mura.android.avantic.di

import com.mura.android.avantic.photo.data.repository.PhotoRepositoryImpl
import com.mura.android.avantic.photo.domain.repository.PhotoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun providePhotoRepository(repo: PhotoRepositoryImpl): PhotoRepository = repo

}