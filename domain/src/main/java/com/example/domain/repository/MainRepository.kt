package com.example.domain.repository

import androidx.lifecycle.LiveData
import com.example.domain.model.EmailData

interface MainRepository {
    fun sendEmail(emailData: EmailData): LiveData<Result<Any?>>
}