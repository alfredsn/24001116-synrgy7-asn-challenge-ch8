package com.example.hublss.worker

import android.content.Context
import android.net.Uri
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.domain.usecase.BlurUseCase

class BlurWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        const val KEY_IMAGE_URI = "IMAGE_URI"
        const val KEY_OUTPUT_URI = "OUTPUT_URI"
    }

    override fun doWork(): Result {
        val inputUriString = inputData.getString(KEY_IMAGE_URI)
        val outputUriString = inputData.getString(KEY_OUTPUT_URI)

        if (inputUriString.isNullOrEmpty() || outputUriString.isNullOrEmpty()) {
            return Result.failure()
        }

        val inputUri = Uri.parse(inputUriString)
        val outputUri = Uri.parse(outputUriString)
        val blurUseCase = BlurUseCase(applicationContext)

        return if (blurUseCase.execute(inputUri, outputUri)) {
            Result.success()
        } else {
            Result.failure()
        }
    }
}