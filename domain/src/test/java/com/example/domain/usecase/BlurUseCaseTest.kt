package com.example.domain.usecase

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.File
import java.io.InputStream

@RunWith(MockitoJUnitRunner::class)
class BlurUseCaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var contentResolver: ContentResolver
    private lateinit var blurUseCase: BlurUseCase

    @Before
    fun prep() {
        context = mock(Context::class.java)
        contentResolver = mock(ContentResolver::class.java)
        `when`(context.contentResolver).thenReturn(contentResolver)
        blurUseCase = BlurUseCase(context)
    }

    @Test
    fun execute_success() {
        val inputUri = mock(Uri::class.java)
        val outputUri = mock(Uri::class.java)
        val inputStream = mock(InputStream::class.java)
        val bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val resizedBitmap = Bitmap.createBitmap(BlurUseCase.TARGET_WIDTH, BlurUseCase.TARGET_HEIGHT, Bitmap.Config.ARGB_8888)
        val outputBitmap = Bitmap.createBitmap(BlurUseCase.TARGET_WIDTH, BlurUseCase.TARGET_HEIGHT, Bitmap.Config.ARGB_8888)
        val outputFile = File.createTempFile("test", ".png")

        `when`(inputUri.path).thenReturn("input_path")
        `when`(outputUri.path).thenReturn(outputFile.path)
        `when`(contentResolver.openInputStream(inputUri)).thenReturn(inputStream)
        `when`(BitmapFactory.decodeStream(inputStream)).thenReturn(bitmap)

        mockStatic(Bitmap::class.java).use { mockedBitmap ->
            mockedBitmap.`when`<Bitmap> { Bitmap.createScaledBitmap(bitmap, BlurUseCase.TARGET_WIDTH, BlurUseCase.TARGET_HEIGHT, true) }
                .thenReturn(resizedBitmap)
            mockedBitmap.`when`<Bitmap> { Bitmap.createBitmap(BlurUseCase.TARGET_WIDTH, BlurUseCase.TARGET_HEIGHT, Bitmap.Config.ARGB_8888) }
                .thenReturn(outputBitmap)
        }

        val result = blurUseCase.execute(inputUri, outputUri)
        assert(result)
    }

    @Test
    fun execute_failure() {
        val inputUri = mock(Uri::class.java)
        val outputUri = mock(Uri::class.java)
        `when`(contentResolver.openInputStream(inputUri)).thenThrow(RuntimeException("Failed to open stream"))

        val result = blurUseCase.execute(inputUri, outputUri)
        assert(!result)
    }
}
