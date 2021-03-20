/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.LocationRequest.LocationResponse
import com.example.androiddevchallenge.LocationStatus.Empty
import com.example.androiddevchallenge.LocationStatus.Loaded
import com.example.androiddevchallenge.LocationStatus.Loading
import com.example.androiddevchallenge.data.CurrentWeatherData
import com.example.androiddevchallenge.data.PreferencesStore
import com.example.androiddevchallenge.data.UserLocation
import com.example.androiddevchallenge.data.WeatherData
import com.example.androiddevchallenge.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

sealed class LocationStatus {
    class Loaded(val data: WeatherData) : LocationStatus()
    object Loading : LocationStatus()
    object Error : LocationStatus()
    object Empty : LocationStatus()
}

sealed class CurrentWeather {
    class Ready(val data: CurrentWeatherData, val lastUpdate: DateTime = DateTime()) : CurrentWeather()
    object Loading : CurrentWeather()
    object Empty : CurrentWeather()
}

@HiltViewModel
class WeatherViewModel @Inject constructor(private val preferencesStore: PreferencesStore) : ViewModel() {

    private val repository = WeatherRepository()

    var userLocation by mutableStateOf<LocationStatus>(Empty)
        private set

    var weather by mutableStateOf<CurrentWeather>(CurrentWeather.Loading)
        private set

    val openSettings = mutableStateOf(false)

    val useCelsius = preferencesStore.celcius

    val firstTime = preferencesStore.firstTime

    val closeApp = mutableStateOf(false)

    init {
        refresh()
    }

    fun setLocation(response: LocationResponse) {

        weather = CurrentWeather.Empty

        viewModelScope.launch {
            userLocation = Loading
            try {
                val location = repository.getLocation(response.lat, response.lon)
                loadWeatherData(location)
                // save user location for next time
                preferencesStore.saveUserLocation(location)
            } catch (e: Exception) {
                e.printStackTrace()
                userLocation = LocationStatus.Error
            }
        }
    }

    private fun loadWeatherData(location: UserLocation) {
        viewModelScope.launch {
            userLocation = try {
                val data = repository.getWeather(location = location)
                Loaded(data)
            } catch (e: Exception) {
                e.printStackTrace()
                LocationStatus.Error
            }

            if (userLocation is Loaded) {

                if (weather is CurrentWeather.Empty && firstTime.first()) {
                    openSettings.value = false
                    preferencesStore.notFirstTime()
                }
                weather = CurrentWeather.Ready(CurrentWeatherData((userLocation as Loaded).data, location))
            }
        }
    }

    fun useCelsius(cecilius: Boolean) {
        viewModelScope.launch {
            preferencesStore.updateUnit(cecilius)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            preferencesStore.userLocation.collect { location ->
                location?.let {
                    loadWeatherData(it)
                } ?: run {
                    weather = CurrentWeather.Empty
                    openSettings.value = true
                }
            }
        }
    }

    fun finish() {
        closeApp.value = true
    }

    fun reload() {
        weather = CurrentWeather.Loading
        refresh()
    }
}
