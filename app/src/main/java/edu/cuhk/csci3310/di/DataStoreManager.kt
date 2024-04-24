package edu.cuhk.csci3310.di

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalTime

typealias KeyDefaultValue<T> = Pair<Preferences.Key<T>, T>

private val Context.dataStore by preferencesDataStore("settings")

object Settings {
    val POST_NOTIFICATIONS = Pair(booleanPreferencesKey("post_notifications"), false)
    val NOTIFY_AT_HOURS = Pair(intPreferencesKey("notify_at_hours"), 16)
    val NOTIFY_AT_MINUTES = Pair(intPreferencesKey("notify_at_minutes"), 30)
    val SHOW_DEBUG_OPTIONS = Pair(booleanPreferencesKey("show_debug_options"), false)
}

class DataStoreManager(appContext: Context) {
    private val settingsDataStore = appContext.dataStore

    suspend fun <T> setValue(key: KeyDefaultValue<T>, value: T) {
        settingsDataStore.edit { preferences ->
            preferences[key.first] = value
        }
    }

    private fun <T> getFlow(key: KeyDefaultValue<T>): Flow<T> {
        return settingsDataStore.data.map { preferences ->
            preferences[key.first] ?: key.second
        }
    }

    val postNotifications = getFlow(Settings.POST_NOTIFICATIONS)
    val notifyAt =
        getFlow(Settings.NOTIFY_AT_HOURS).combine(getFlow(Settings.NOTIFY_AT_MINUTES)) { hours, minutes ->
            LocalTime.of(hours, minutes)
        }
    val showDebugOptions = getFlow(Settings.SHOW_DEBUG_OPTIONS)
}

