package com.mura.android.avantic.utils.extentions

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.IOException

fun Activity.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Activity.hasCamera() =
    this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
