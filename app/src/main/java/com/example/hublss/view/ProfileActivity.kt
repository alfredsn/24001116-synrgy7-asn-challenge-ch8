package com.example.hublss.view

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.hublss.databinding.ActivityProfileBinding
import com.example.hublss.viewmodel.BlurViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    private lateinit var blurViewModel: BlurViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        blurViewModel = ViewModelProvider(this)[BlurViewModel::class.java]

        binding.btnChoosePicture.setOnClickListener {
            checkPermissionAndOpenGallery()
        }

        binding.btnBlurPicture.setOnClickListener {
            blurImage()
        }

        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openGallery()
            } else {
                showPermissionDeniedDialog()
            }
        }

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                binding.ivProfilePicture.setImageURI(uri)
                binding.ivProfilePicture.tag = uri
            }
        }

        blurViewModel.outputUri.observe(this) { uri ->
            uri?.let {
                binding.ivProfilePicture.setImageURI(uri)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun checkPermissionAndOpenGallery() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED -> {
                openGallery()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                showPermissionRationale()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }

    private fun blurImage() {
        val inputUri = getImageUri()
        if (inputUri == null) {
            showNoImageSelectedError()
            return
        }

        val outputUri = blurViewModel.createOutputUri()
        blurViewModel.blurImage(inputUri, outputUri)
    }

    private fun showNoImageSelectedError() {
        AlertDialog.Builder(this)
            .setMessage("Please select an image before blurring.")
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    private fun getImageUri(): Uri? {
        return binding.ivProfilePicture.tag as? Uri
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setMessage("Storage access permission is needed to choose a picture.")
            .setPositiveButton("OK") { _, _ -> }
            .show()
    }

    private fun showPermissionRationale() {
        AlertDialog.Builder(this)
            .setMessage("Storage access permission is required to choose a picture.")
            .setPositiveButton("Grant") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}