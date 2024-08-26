package com.example.domain.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import android.content.Context
import com.example.domain.datastore.DataStoreManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginUseCaseTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var context: Context
    private lateinit var dataStoreManager: DataStoreManager
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun prep() {
        context = mock(Context::class.java)
        dataStoreManager = mock(DataStoreManager::class.java)
        loginUseCase = LoginUseCase(dataStoreManager)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loginAction_success() = runTest {
        `when`(dataStoreManager.saveLoginStatus(context, true)).thenReturn(Unit)

        val result = loginUseCase.loginAction(context, "admin", "admin123")
        assertTrue(result.isSuccess)
        verify(dataStoreManager).saveLoginStatus(context, true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loginAction_failure() = runTest {
        val result = loginUseCase.loginAction(context, "wrong_user", "wrong_pass")
        assertTrue(result.isFailure)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun validateLogin_success() = runTest {
        `when`(dataStoreManager.saveLoginStatus(context, true)).thenReturn(Unit)

        var onSuccessCalled = false
        var onFailureCalled = false

        loginUseCase.validateLogin(context, "admin", "admin123",
            onSuccess = { onSuccessCalled = true },
            onFailure = { onFailureCalled = true }
        )

        assertTrue(onSuccessCalled)
        assertFalse(onFailureCalled)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun validateLogin_failure() = runTest {
        var onSuccessCalled = false
        var onFailureCalled = false

        loginUseCase.validateLogin(context, "wrong_user", "wrong_pass",
            onSuccess = { onSuccessCalled = true },
            onFailure = { onFailureCalled = true }
        )

        assertFalse(onSuccessCalled)
        assertTrue(onFailureCalled)
    }
}
