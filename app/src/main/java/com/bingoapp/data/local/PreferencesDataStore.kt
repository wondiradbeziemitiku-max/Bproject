package com.bingoapp.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "bingo_prefs")

@Singleton
class PreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val KEY_IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        val KEY_LAST_GAME_ID = stringPreferencesKey("last_game_id")
        val KEY_LAST_LOGIN_TIME = longPreferencesKey("last_login_time")
        val KEY_SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    }

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_IS_FIRST_LAUNCH] ?: true
    }

    val soundEnabled: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_SOUND_ENABLED] ?: true
    }

    val lastGameId: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[KEY_LAST_GAME_ID] ?: ""
    }

    suspend fun setFirstLaunchDone() {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_FIRST_LAUNCH] = false
        }
    }

    suspend fun setLastGameId(gameId: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_GAME_ID] = gameId
        }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SOUND_ENABLED] = enabled
        }
    }

    suspend fun updateLastLoginTime() {
        context.dataStore.edit { prefs ->
            prefs[KEY_LAST_LOGIN_TIME] = System.currentTimeMillis()
        }
    }
}
