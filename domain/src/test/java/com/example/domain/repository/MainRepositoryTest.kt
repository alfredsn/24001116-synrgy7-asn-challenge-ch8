package com.example.domain.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.domain.model.EmailData
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito.*

class MainRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val repository: MainRepository = mock(MainRepository::class.java)
    private val emailData: EmailData = mock(EmailData::class.java)

    @Test
    fun sendEmail_success() {
        val expectedResult = MutableLiveData<Result<Any?>>()
        expectedResult.value = Result.success(null)

        `when`(repository.sendEmail(emailData)).thenReturn(expectedResult)

        val result: LiveData<Result<Any?>> = repository.sendEmail(emailData)

        assert(result.value?.isSuccess == true)
        verify(repository).sendEmail(emailData)
    }

    @Test
    fun sendEmail_failure() {
        val expectedResult = MutableLiveData<Result<Any?>>()
        expectedResult.value = Result.failure(Exception("Failed to send email"))

        `when`(repository.sendEmail(emailData)).thenReturn(expectedResult)

        val result: LiveData<Result<Any?>> = repository.sendEmail(emailData)

        assert(result.value?.isFailure == true)
        verify(repository).sendEmail(emailData)
    }
}