package com.example.hublss.appkoin

import com.example.data.repository.MainRepositoryImpl
import com.example.domain.repository.MainRepository
import com.example.hublss.viewmodel.LoginViewModel
import com.example.hublss.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    single<MainRepository> { MainRepositoryImpl() }
}