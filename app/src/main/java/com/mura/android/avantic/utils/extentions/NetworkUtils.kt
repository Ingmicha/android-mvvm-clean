package com.mura.android.avantic.utils.extentions

import com.mura.android.avantic.utils.response.ResultManager
import java.io.IOException

suspend fun <T : Any> safeApiCall(
    call: suspend () -> ResultManager<T>,
    errorMessage: String
): ResultManager<T> {
    return try {
        call()
    } catch (e: Exception) {
        ResultManager.Exception(IOException(errorMessage, e))
    }
}