package com.teknasyon.desk360.helper

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

@SuppressLint("NewApi")
@TargetApi(Build.VERSION_CODES.KITKAT)
class ImageFilePath {
    /* Get uri related content real local file path. */
    fun getUriRealPath(ctx: Context, uri: Uri): String {
        var ret = ""
        ret = if (isAboveKitKat) { // Android OS above sdk version 19.
            getUriRealPathAboveKitkat(ctx, uri)
        } else { // Android OS below sdk version 19
            getImageRealPath(ctx.contentResolver, uri, null)
        }
        return ret
    }

    private fun getUriRealPathAboveKitkat(
        ctx: Context?,
        uri: Uri?
    ): String {
        var ret: String? = ""
        if (ctx != null && uri != null) {
            if (isContentUri(uri)) {
                ret = if (uri.authority?.let { isGooglePhotoDoc(it) } == true) {
                    uri.lastPathSegment
                } else {
                    getImageRealPath(ctx.contentResolver, uri, null)
                }
            } else if (isFileUri(uri)) {
                ret = uri.path
            } else if (isDocumentUri(ctx, uri)) { // Get uri related document id.
                val documentId = DocumentsContract.getDocumentId(uri)
                // Get uri authority.
                val uriAuthority = uri.authority
                if (uriAuthority?.let { isMediaDoc(it) } == true) {
                    val idArr = documentId.split(":").toTypedArray()
                    if (idArr.size == 2) { // First item is document type.
                        val docType = idArr[0]
                        // Second item is document real id.
                        val realDocId = idArr[1]
                        // Get content uri by document type.
                        var mediaContentUri =
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        when (docType) {
                            "image" -> {
                                mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            "video" -> {
                                mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }
                            "audio" -> {
                                mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                        }
                        // Get where clause with real document id.
                        val whereClause =
                            MediaStore.Images.Media._ID + " = " + realDocId
                        ret =
                            getImageRealPath(ctx.contentResolver, mediaContentUri, whereClause)
                    }
                } else if (uriAuthority?.let { isDownloadDoc(it) } == true) { // Build download uri.
                    val downloadUri =
                        Uri.parse("content://downloads/public_downloads")
                    // Append download document id at uri end.
                    val downloadUriAppendId =
                        ContentUris.withAppendedId(downloadUri, java.lang.Long.valueOf(documentId))
                    ret = getImageRealPath(ctx.contentResolver, downloadUriAppendId, null)
                } else if (uriAuthority?.let { isExternalStoreDoc(it) } == true) {
                    val idArr = documentId.split(":").toTypedArray()
                    if (idArr.size == 2) {
                        val type = idArr[0]
                        val realDocId = idArr[1]
                        if ("primary".equals(type, ignoreCase = true)) {
                            ret =
                                Environment.getExternalStorageDirectory()
                                    .toString() + "/" + realDocId
                        }
                    }
                }
            }
        }
        return ret ?: ""
    }

    /* Check whether current android os version is bigger than kitkat or not. */
    private val isAboveKitKat: Boolean
        get() {
            var ret = false
            ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            return ret
        }

    /* Check whether this uri represent a document or not. */
    private fun isDocumentUri(ctx: Context?, uri: Uri?): Boolean {
        var ret = false
        if (ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri)
        }
        return ret
    }

    /* Check whether this uri is a content uri or not.
     *  content uri like content://media/external/images/media/1302716
     *  */
    private fun isContentUri(uri: Uri?): Boolean {
        var ret = false
        if (uri != null) {
            val uriSchema = uri.scheme
            if ("content".equals(uriSchema, ignoreCase = true)) {
                ret = true
            }
        }
        return ret
    }

    /* Check whether this uri is a file uri or not.
     *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
     * */
    private fun isFileUri(uri: Uri?): Boolean {
        var ret = false
        if (uri != null) {
            val uriSchema = uri.scheme
            if ("file".equals(uriSchema, ignoreCase = true)) {
                ret = true
            }
        }
        return ret
    }

    /* Check whether this document is provided by ExternalStorageProvider. */
    fun isExternalStoreDoc(uriAuthority: String): Boolean {
        var ret = false
        if ("com.android.externalstorage.documents" == uriAuthority) {
            ret = true
        }
        return ret
    }

    /* Check whether this document is provided by DownloadsProvider. */
    private fun isDownloadDoc(uriAuthority: String): Boolean {
        var ret = false
        if ("com.android.providers.downloads.documents" == uriAuthority) {
            ret = true
        }
        return ret
    }

    /* Check whether this document is provided by MediaProvider. */
    private fun isMediaDoc(uriAuthority: String): Boolean {
        var ret = false
        if ("com.android.providers.media.documents" == uriAuthority) {
            ret = true
        }
        return ret
    }

    /* Check whether this document is provided by google photos. */
    private fun isGooglePhotoDoc(uriAuthority: String): Boolean {
        var ret = false
        if ("com.google.android.apps.photos.content" == uriAuthority) {
            ret = true
        }
        return ret
    }

    /* Return uri represented document file real local path.*/
    private fun getImageRealPath(
        contentResolver: ContentResolver,
        uri: Uri,
        whereClause: String?
    ): String {
        var ret = ""
        // Query the uri with condition.
        val cursor =
            contentResolver.query(uri, null, whereClause, null, null)
        if (cursor != null) {
            val moveToFirst = cursor.moveToFirst()
            if (moveToFirst) { // Get columns name by uri type.
                var columnName = MediaStore.Images.Media._ID
                var contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                when (uri) {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI -> {
                        columnName = MediaStore.Images.Media._ID
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    }
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI -> {
                        columnName = MediaStore.Audio.Media._ID
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI -> {
                        columnName = MediaStore.Video.Media._ID
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    }
                }
                // Get column index.
                val imageColumnIndex = cursor.getColumnIndex(columnName)
                val id = cursor.getLong(imageColumnIndex)
                ret = ContentUris.withAppendedId(contentUri, id).toString()
            }
        }
        return ret
    }

    fun getFileName(
        contentResolver: ContentResolver,
        uri: Uri,
    ): String {
        var fileName = ""
        val cursor =
            contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            val moveToFirst = cursor.moveToFirst()
            if (moveToFirst) {
                val imageNameIndex =
                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
                fileName = cursor.getString(imageNameIndex)
            }
        }
        return fileName
    }
}


