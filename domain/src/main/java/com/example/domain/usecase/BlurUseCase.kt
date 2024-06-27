package com.example.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class BlurUseCase(private val context: Context) {

    companion object {
        const val TARGET_WIDTH = 850
        const val TARGET_HEIGHT = 800
    }

    fun execute(inputUri: Uri, outputUri: Uri): Boolean {
        return try {
            val bitmap = decodeBitmapFromUri(inputUri) ?: return false
            val resizedBitmap = resizeBitmap(bitmap, TARGET_WIDTH, TARGET_HEIGHT)
            val blurredBitmap = blurBitmap(resizedBitmap)
            saveBitmapToFile(blurredBitmap, File(outputUri.path!!))
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun decodeBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val contentResolver = context.contentResolver
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            inputStream?.use {
                BitmapFactory.decodeStream(it)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun blurBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(output)
        val paint = Paint().apply {
            isAntiAlias = true
            maskFilter = BlurMaskFilter(80f, BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return output
    }

    private fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    private fun saveBitmapToFile(bitmap: Bitmap, outputFile: File) {
        FileOutputStream(outputFile).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
    }
}