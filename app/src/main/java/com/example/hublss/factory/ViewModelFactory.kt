package com.example.hublss.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.repository.MainRepositoryImpl
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.LogoutUseCase
import com.example.hublss.viewmodel.BlurViewModel
import com.example.hublss.viewmodel.LoginViewModel
import com.example.hublss.viewmodel.MainViewModel

class ViewModelFactory(
    private val application: Application,
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val mainRepositoryImpl: MainRepositoryImpl
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BlurViewModel::class.java) -> {
                BlurViewModel(application) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(loginUseCase, logoutUseCase) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(mainRepositoryImpl) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}