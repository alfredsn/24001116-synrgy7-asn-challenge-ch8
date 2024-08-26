package com.example.hublss.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.domain.model.EmailData
import com.example.domain.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: MainRepository

    @Mock
    private lateinit var sendEmailObserver: Observer<Result<Any?>>

    private lateinit var viewModel: MainViewModel

    @Before
    fun prep() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(repository)
    }

    @Test
    fun sendEmail_success() = runBlockingTest {
        val emailData = object : EmailData {
            override val sendTo: String = "recipient@example.com"
            override val name: String = "Hauftt Hublaaa"
            override val replyTo: String = "noreply@hublss.com"
            override val isHtml: Boolean = false
            override val title: String = "Subject"
            override val body: String = "Body of the email"
        }

        val successResult = Result.success<Any?>(null)

        `when`(repository.sendEmail(emailData)).thenReturn(Mockito.mock(LiveData::class.java) as LiveData<Result<Any?>>)

        viewModel.sendEmail(emailData)

        viewModel.sendEmailResult.observeForever(sendEmailObserver)

        sendEmailObserver.onChanged(successResult)

        viewModel.sendEmailResult.removeObserver(sendEmailObserver)
    }
}
