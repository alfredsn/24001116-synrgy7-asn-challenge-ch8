package com.example.hublss.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.hublss.worker.BlurWorker
import java.io.File

class BlurViewModel(application: Application) : AndroidViewModel(application) {

    private val workManager = WorkManager.getInstance(application)

    private val _outputUri = MutableLiveData<Uri?>()
    val outputUri: LiveData<Uri?> get() = _outputUri

    fun blurImage(inputUri: Uri, outputUri: Uri) {
        val inputData = Data.Builder()
            .putString(BlurWorker.KEY_IMAGE_URI, inputUri.toString())
            .putString(BlurWorker.KEY_OUTPUT_URI, outputUri.toString())
            .build()

        val blurWorkRequest = OneTimeWorkRequestBuilder<BlurWorker>()
            .setInputData(inputData)
            .build()

        workManager.enqueue(blurWorkRequest)

        workManager.getWorkInfoByIdLiveData(blurWorkRequest.id).observeForever { workInfo ->
            if (workInfo != null && workInfo.state.isFinished) {
                if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                    _outputUri.value = outputUri
                } else {
                    _outputUri.value = null
                }
            }
        }
    }

    fun createOutputUri(): Uri {
        val outputDir = File(getApplication<Application>().filesDir, "output_images")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, "blurred_image_${System.currentTimeMillis()}.png")
        return Uri.fromFile(outputFile)
    }
}