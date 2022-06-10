
package com.example.androiddevchallenge.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesStore @Inject constructor(@ApplicationContext private val context: Context) {

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val Context.dataStore by preferencesDataStore("weather")

    private object PreferencesKeys {
        val CELCIUS = booleanPreferencesKey("celcius")
        val FIRST_TIME = booleanPreferencesKey("first_time")
        val USER_LOCATION = stringPreferencesKey("user_location")
    }

    val celcius = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.CELCIUS] ?: true
    }

    val firstTime = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.FIRST_TIME] ?: true
    }

    suspend fun notFirstTime() {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.FIRST_TIME] = false
        }
    }

    suspend fun updateUnit(celcius: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.CELCIUS] = celcius
        }
    }

    val userLocation = context.dataStore.data.map { preferences ->
        val locationVal = preferences[PreferencesKeys.USER_LOCATION]
        locationVal?.let { gson.fromJson(it, UserLocation::class.java) }
    }

    suspend fun saveUserLocation(location: UserLocation) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_LOCATION] = gson.toJson(location)
        }
    }

//    .catch { exception ->
//        // dataStore.data throws an IOException when an error is encountered when reading data
//        if (exception is IOException) {
//            emit(emptyPreferences())
//        } else {
//            throw exception
//        }
//    }
}
