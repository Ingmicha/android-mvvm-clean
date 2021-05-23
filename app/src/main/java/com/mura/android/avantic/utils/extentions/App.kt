package com.mura.android.avantic.utils.extentions

import android.app.Activity
import android.content.pm.PackageManager
import android.widget.Toast

fun Activity.toast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Activity.hasCamera() =
    this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
