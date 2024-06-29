package com.example.koin

import com.example.data.repository.MainRepositoryImpl
import com.example.domain.datastore.DataStoreManager
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.LogoutUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val hublssModules = module {
    single<DataStoreManager> { DataStoreManager }
    single { MainRepositoryImpl() }
    single { LoginUseCase(get()) }
    single { LogoutUseCase(get()) }
}