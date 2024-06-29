package com.example.domain.usecase

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.datastore.DataStoreManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LogoutUseCaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var logoutUseCase: LogoutUseCase

    @Before
    fun prep() {
        context = mock(Context::class.java)
        dataStoreManager = mock(DataStoreManager::class.java)
        logoutUseCase = LogoutUseCase(dataStoreManager)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun logoutAction_success() = runTest {
        doNothing().`when`(dataStoreManager).saveLoginStatus(context, false)

        val result = logoutUseCase.logoutAction(context)
        assertTrue(result.isSuccess)
        verify(dataStoreManager).saveLoginStatus(context, false)
    }
}
