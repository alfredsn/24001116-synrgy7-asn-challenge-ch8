package com.example.hublss.worker

import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.work.Data
import androidx.work.WorkerParameters
import com.example.domain.usecase.BlurUseCase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BlurWorkerTest {

    private lateinit var context: Context
    private lateinit var workerParams: WorkerParameters
    private lateinit var blurUseCase: BlurUseCase

    @Before
    fun prep() {
        context = ApplicationProvider.getApplicationContext()
        workerParams = mock(WorkerParameters::class.java)
        blurUseCase = mock(BlurUseCase::class.java)
    }

    @Test
    fun doWork_success() {
        val inputUriString = "content://input_uri"
        val outputUriString = "content://output_uri"

        val inputData = Data.Builder()
            .putString(BlurWorker.KEY_IMAGE_URI, inputUriString)
            .putString(BlurWorker.KEY_OUTPUT_URI, outputUriString)
            .build()

        `when`(workerParams.inputData).thenReturn(inputData)
        `when`(blurUseCase.execute(Uri.parse(inputUriString), Uri.parse(outputUriString))).thenReturn(true)

        val worker = BlurWorker(context, workerParams)
        val result = worker.doWork()

        assertTrue(result is androidx.work.ListenableWorker.Result.Success)
    }

    @Test
    fun doWork_failure_missingInputData() {
        val inputData = Data.Builder().build()

        `when`(workerParams.inputData).thenReturn(inputData)

        val worker = BlurWorker(context, workerParams)
        val result = worker.doWork()

        assertTrue(result is androidx.work.ListenableWorker.Result.Failure)
    }

    @Test
    fun doWork_failure_blurExecutionFailed() {
        val inputUriString = "content://input_uri"
        val outputUriString = "content://output_uri"

        val inputData = Data.Builder()
            .putString(BlurWorker.KEY_IMAGE_URI, inputUriString)
            .putString(BlurWorker.KEY_OUTPUT_URI, outputUriString)
            .build()

        `when`(workerParams.inputData).thenReturn(inputData)
        `when`(blurUseCase.execute(Uri.parse(inputUriString), Uri.parse(outputUriString))).thenReturn(false)

        val worker = BlurWorker(context, workerParams)
        val result = worker.doWork()

        assertTrue(result is androidx.work.ListenableWorker.Result.Failure)
    }
}
