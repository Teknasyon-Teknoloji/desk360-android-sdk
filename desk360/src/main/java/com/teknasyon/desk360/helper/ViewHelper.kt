package com.teknasyon.desk360.helper

import android.content.Context
import android.graphics.Typeface
import android.util.DisplayMetrics
import android.webkit.MimeTypeMap
import androidx.core.net.toUri
import com.teknasyon.desk360.R
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File

fun Int?.toTypeFace(context: Context): Typeface {
    val font = when (this) {
        100 -> "Montserrat-Thin.ttf"
        200 -> "Montserrat-ExtraLight.ttf"
        300 -> "Montserrat-Light.ttf"
        400 -> "Montserrat-Regular.ttf"
        500 -> "Montserrat-Medium.ttf"
        600 -> "Montserrat-SemiBold.ttf"
        700 -> "Montserrat-Bold.ttf"
        800 -> "Montserrat-ExtraBold.ttf"
        900 -> "Montserrat-Black.ttf"
        else -> "Montserrat-Regular.ttf"
    }

    return Typeface.createFromAsset(context.assets, font)
}

fun Context.convertDpToPixel(dp: Float): Float {
    return dp * (resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
}

fun String.getFileIconResource(): Int {
    return when {
        endsWith("jpg", true) || endsWith("jpeg", true) -> R.drawable.ic_jpg
        endsWith("gif", true) -> R.drawable.gif
        endsWith("bmp", true) -> R.drawable.bmp
        endsWith("png", true) -> R.drawable.ic_png
        endsWith("mp4", true) -> R.drawable.mp4
        endsWith("avi", true) -> R.drawable.ic_avi
        endsWith("flv", true) -> R.drawable.flv
        endsWith("pdf", true) -> R.drawable.pdf
        endsWith("doc", true) -> R.drawable.doc
        endsWith("docx", true) -> R.drawable.docx
        endsWith("xls", true) -> R.drawable.xls
        endsWith("xlsx", true) -> R.drawable.xlsx
        endsWith("zip", true) -> R.drawable.zip
        endsWith("gzip", true) -> R.drawable.gzip
        endsWith("rar", true) -> R.drawable.rar
        else -> R.drawable.others
    }
}

fun File.getMediaType(): MediaType? {
    val ext = MimeTypeMap.getFileExtensionFromUrl(toUri().toString())
    return when (ext) {
        "jpg", "jpeg" -> "image/jpg".toMediaTypeOrNull()
        "gif" -> "image/gif".toMediaTypeOrNull()
        "bmp" -> "image/bmp".toMediaTypeOrNull()
        "png" -> "image/png".toMediaTypeOrNull()
        "mp4" -> "video/mp4".toMediaTypeOrNull()
        "avi" -> "video/avi".toMediaTypeOrNull()
        "flv" -> "video/flv".toMediaTypeOrNull()
        "pdf" -> "application/pdf".toMediaTypeOrNull()
        "doc" -> "application/doc".toMediaTypeOrNull()
        "docx" -> "application/docx".toMediaTypeOrNull()
        "xls" -> "application/xls".toMediaTypeOrNull()
        "xlsx" -> "application/xlsx".toMediaTypeOrNull()
        "zip" -> "application/zip".toMediaTypeOrNull()
        "gzip" -> "application/gzip".toMediaTypeOrNull()
        "rar" -> "application/rar".toMediaTypeOrNull()
        else -> null
    }
}