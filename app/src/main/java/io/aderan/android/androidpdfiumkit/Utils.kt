package io.aderan.android.androidpdfiumkit

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.OutputStreamWriter

object Utils {

    @Throws(IOException::class)
    fun fileFromAsset(context: Context, assetName: String): File {
        val outFile = File(context.cacheDir, "$assetName-pdfview.pdf")
        if (assetName.contains("/")) {
            outFile.parentFile.mkdirs()
        }
        copy(context.assets.open(assetName), FileOutputStream(outFile))
        return outFile
    }

    @Throws(IOException::class)
    fun copy(inputStream: InputStream?, outputStream: OutputStream) {
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }

    fun getInternalFilePath(context: Context, fileName: String): String {
        return File(context.filesDir, fileName).path
    }

    fun writeStringToFile(filePath: String, content: String) {
        try {
            FileOutputStream(filePath, true).use { fos ->
                OutputStreamWriter(fos).use { outputStreamWriter ->
                    outputStreamWriter.write(content)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor: Cursor = context.contentResolver.query(
                uri,
                null,
                null,
                null,
                null
            ) ?: return "null"
            cursor.use { c ->
                if (c.moveToFirst()) {
                    val columnIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    result = c.getString(columnIndex)
                }
            }
        }
        if (result == null) {
            result = uri.lastPathSegment
        }
        return result
    }

}