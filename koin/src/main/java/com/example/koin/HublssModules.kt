package com.example.koin

import com.example.data.repository.MainRepositoryImpl
import com.example.domain.datastore.DataStoreManager
import com.example.domain.usecase.BlurUseCase
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.LogoutUseCase
import org.koin.dsl.module

val hublssModules = module {

    single { MainRepositoryImpl() }
    single { BlurUseCase(get()) }
    single { LoginUseCase(get()) }
    single { LogoutUseCase(get()) }
    single { DataStoreManager }
}