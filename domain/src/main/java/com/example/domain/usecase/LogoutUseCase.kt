package com.example.domain.usecase

import android.content.Context
import com.example.domain.datastore.DataStoreManager

class LogoutUseCase(private val dataStoreManager: DataStoreManager) {

    suspend fun logoutAction(context: Context): Result<Unit> {
        dataStoreManager.saveLoginStatus(context, false)
        return Result.success(Unit)
    }
}