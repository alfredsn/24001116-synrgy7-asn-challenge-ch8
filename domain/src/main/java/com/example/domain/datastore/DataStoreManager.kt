package com.example.domain.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object DataStoreManager {
    private const val DATASTORE_NAME = "user_preferences"
    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

    private val LOGIN_STATUS_KEY = booleanPreferencesKey("login_status")

    suspend fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_STATUS_KEY] = isLoggedIn
        }
    }

    fun getLoginStatus(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[LOGIN_STATUS_KEY] ?: false
        }
    }
}