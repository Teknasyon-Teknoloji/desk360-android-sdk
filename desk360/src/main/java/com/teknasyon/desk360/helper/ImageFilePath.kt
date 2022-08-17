package com.teknasyon.desk360.helper

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.teknasyon.desk360.model.FileResource
import java.io.File
import java.io.FileOutputStream

class ImageFilePath {

    private fun getFileName(
        contentResolver: ContentResolver,
        uri: Uri
    ): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        var fileName = ""
        if (cursor != null) {
            val moveToFirst = cursor.moveToFirst()
            if (moveToFirst) {
                fileName = try {
                    val imageNameIndex =
                        cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                    cursor.getString(imageNameIndex)
                } catch (e: Exception) {
                    ""
                }

            }
        }
        return fileName
    }


    private fun writeToFile(fileName: String, uri: Uri, context: Context): File {
        val outputFile = File.createTempFile(fileName, "")
        val outputStream = FileOutputStream(outputFile)
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
        }
        catch (e:java.lang.Exception){
            Log.e("Exception", e.message.toString())
        }
        return outputFile
    }

    fun createFile(
        uri: Uri,
        context: Context
    ): FileResource {
        val fileResource: FileResource?
        var fileName = "content"
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.Q)
            fileName =
                ImageFilePath().getFileName(context.contentResolver, uri)
        fileResource = FileResource(writeToFile(fileName, uri, context), fileName)

        return fileResource
    }
}


