package com.mura.android.avantic.utils.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mura.android.avantic.photo.data.database.PhotoDao
import com.mura.android.avantic.photo.domain.model.PhotoData
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: PhotoDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        dao = db.photoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun crudTest() = runBlocking {
        val photo = PhotoData(
            albumId = (0..10).random().toString(),
            title = (11..20).random().toString(),
            url = (21..30).random().toString(),
            thumbnailUrl = (30..40).random().toString(),
            id = (41..50).random()
        )
        assert(dao.insert(photo) != 0L)
        assert(dao.selectAll().isNotEmpty())
        assert(dao.updateTitleById(photo.title, photo.id) != 0)
        assert(dao.deletePhoto(photo.id.toString()) != 0)
        assert(dao.selectAll().isEmpty())
    }
}