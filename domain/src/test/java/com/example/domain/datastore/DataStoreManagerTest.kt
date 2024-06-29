package com.example.domain.datastore

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class DataStoreManagerTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var context: Context

    @Before
    fun prep() {
        context = ApplicationProvider.getApplicationContext()
        File(context.filesDir, "datastore/${DataStoreManager.DATASTORE_NAME}.preferences_pb").delete()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun saveLoginStatus() = runTest {
        DataStoreManager.saveLoginStatus(context, true)
        val loginStatus = DataStoreManager.getLoginStatus(context).first()
        assertEquals(true, loginStatus)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getLoginStatus_defaultValue() = runTest {
        val loginStatus = DataStoreManager.getLoginStatus(context).first()
        assertEquals(false, loginStatus)
    }
}