package com.teknasyon.desk360.helper

import android.app.Activity
import android.content.Context
import android.media.MediaScannerConnection
import android.net.Uri
import android.provider.MediaStore
import java.io.File

/* Get uri related content real local file path. */
fun Context.getFileName(uri: Uri): String? {
    val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME)
    val metaCursor = contentResolver.query(uri, projection, null, null, null)
    metaCursor?.use {
        if (metaCursor.moveToFirst()) {
            return@getFileName metaCursor.getString(0)
        }
    }
    return null
}

fun Context.getFile(uri: Uri): File? {
    val inputStream = contentResolver.openInputStream(uri) ?: return null
    val file = File.createTempFile("dsk360_", getFileName(uri), cacheDir)
    file.outputStream().use {
        val bytes = inputStream.readBytes()
        it.write(bytes)
    }
    return file
}

object ImageFilePath {
    fun refreshGallery(path: String, activity: Activity) {

        val f = File(path)
        MediaScannerConnection.scanFile(
            activity, arrayOf(f.toString()),
            arrayOf(f.name), null
        )
    }
}