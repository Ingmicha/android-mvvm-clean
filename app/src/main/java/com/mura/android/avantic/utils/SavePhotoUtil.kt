package com.mura.android.avantic.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

object SavePhotoUtil {
    fun saveBitmap(
        @NonNull context: Context,
        @NonNull bitmap: Bitmap,
        @NonNull displayName: String
    ): Uri? {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
            values.put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
            values.put(MediaStore.Images.Media.RELATIVE_PATH, getMainDirectoryName())
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.
            val uri: Uri? =
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
            return uri
        } else {
            val directory = File(
                context.getExternalFilesDir(null)?.absolutePath?.replace(
                    "/Android/data/" + context.packageName + "/files",
                    "/" + Environment.DIRECTORY_PICTURES + "/KuboWallet"
                )
                    ?: ""
            )
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, displayName)

            saveImageToStream(bitmap, FileOutputStream(file))
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, file.absolutePath)
            // .DATA is deprecated in API 29
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            return file.toUri()
        }
    }

    /*  Create Directory where screenshot will save for sharing screenshot  */
    private fun getMainDirectoryName(): String {
        //Here we will use getExternalFilesDir and inside that we will make our Demo folder
        //benefit of getExternalFilesDir is that whenever the app uninstalls the images will get deleted automatically.

        val relativeLocation = Environment.DIRECTORY_PICTURES + "/KuboWallet"

        val mainDir = File(relativeLocation)

        //If File is not present create directory
        if (!mainDir.exists()) {
            if (mainDir.mkdir()) Log.e("Create Directory", "Main Directory Created : $mainDir")
        }

        return relativeLocation
    }


    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}